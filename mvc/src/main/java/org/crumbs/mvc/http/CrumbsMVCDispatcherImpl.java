package org.crumbs.mvc.http;

import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.core.logging.Logger;
import org.crumbs.json.JsonMapper;
import org.crumbs.mvc.common.model.HttpStatus;
import org.crumbs.mvc.common.model.Mime;
import org.crumbs.mvc.context.handler.Handler;
import org.crumbs.mvc.context.handler.HandlerContext;
import org.crumbs.mvc.context.handler.HandlerInvocationResult;
import org.crumbs.mvc.exception.*;
import org.crumbs.mvc.model.ResponseEntity;


public class CrumbsMVCDispatcherImpl implements CrumbsMVCDispatcher {

    private Logger logger = Logger.getLogger(CrumbsMVCDispatcherImpl.class);

    private JsonMapper jsonMapper = new JsonMapper();

    @CrumbRef
    private HandlerContext handlerContext;

    @Override
    public void handle(Request request, Response response) {

        logger.debug("Handling request {} {}", request.getMethod(), request.getUrlPath());
        long start = System.currentTimeMillis();

        try {
            if (!handlerContext.intercept(request, response)) {
                return;
            }

            Handler handler = handlerContext.findHandler(request);
            if(handler == null) {
                throw new NotFoundException("Could not find mapping for: " + request.getUrlPath());
            }
            if(!handler.getHttpMethod().equals(request.getMethod())) {
                throw new HttpMethodNotAllowedException("Http method " + request.getMethod() +
                        " not allowed for request " + request.getUrlPath());
            }

            HandlerInvocationResult result = invokeHandler(request, handler);

            HttpStatus status;
            Object body;
            if (result.getReturnType().equals(String.class)) {
                status = HttpStatus.OK;
                body = result.getContent();
            } else if (result.getReturnType().equals(ResponseEntity.class)) {
                ResponseEntity<?> responseEntity = (ResponseEntity<?>) result.getContent();
                status = responseEntity.getStatus();
                body = jsonMapper.marshal(responseEntity.getBody());
            } else if (result.getReturnType() == null) {
                status = HttpStatus.NO_CONTENT;
                body = null;
            } else {
                throw new Exception("Return type not supported");
            }

            response.setStatus(status);
            response.setMime(result.getMime());
            response.setBody(body == null ? new byte[]{} : body.toString().getBytes());
        } catch (BadRequestException e) {
            logger.warn(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMime(Mime.TEXT_PLAIN);
            response.setBody(e.getMessage().getBytes());
        } catch (ConflictException e) {
            logger.warn(e.getMessage());
            response.setStatus(HttpStatus.CONFLICT);
            response.setMime(Mime.TEXT_PLAIN);
            response.setBody(e.getMessage().getBytes());
        } catch (ForbiddenException e) {
            logger.warn(e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN);
            response.setMime(Mime.TEXT_PLAIN);
            response.setBody(e.getMessage().getBytes());
        } catch (UnauthorizedException e) {
            logger.warn(e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setMime(Mime.TEXT_PLAIN);
            response.setBody(e.getMessage().getBytes());
        } catch (NotFoundException e) {
            logger.warn(e.getMessage());
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setMime(Mime.TEXT_PLAIN);
            response.setBody(e.getMessage().getBytes());
        } catch (InternalServerErrorException e) {
            logger.warn(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMime(Mime.TEXT_PLAIN);
            response.setBody(e.getMessage().getBytes());
        } catch (HandlerNotFoundException ex) {
            logger.warn(ex.getMessage());
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setMime(Mime.TEXT_PLAIN);
            response.setBody("Resource not found".getBytes());

        } catch (HttpMethodNotAllowedException ex) {
            logger.warn(ex.getMessage());
            response.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
            response.setMime(Mime.TEXT_PLAIN);
            response.setBody("Method not allowed for current mapping".getBytes());

        } catch (HandlerInvocationException ex) {
            logger.error(ex.getMessage(), ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMime(Mime.TEXT_PLAIN);
            response.setBody("An internal server error occurred".getBytes());

        } catch (Exception ex) {
            logger.error("An internal server error occurred", ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMime(Mime.TEXT_PLAIN);
            response.setBody("An internal server error occurred".getBytes());

        }
        long end = System.currentTimeMillis() - start;
        logger.debug("Handled request {} {} in {} ms", request.getMethod(), request.getUrlPath(), end);
    }


    private HandlerInvocationResult invokeHandler(Request request, Handler handler) throws Exception {
        return handler.invoke(request);
    }
}
