package org.crumbs.http.mvc.http;

import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.core.logging.Logger;
import org.crumbs.http.common.model.HttpMethod;
import org.crumbs.http.common.model.HttpStatus;
import org.crumbs.http.common.model.Mime;
import org.crumbs.http.mvc.context.handler.Handler;
import org.crumbs.http.mvc.context.handler.HandlerContext;
import org.crumbs.http.mvc.context.handler.HandlerInvocationResult;
import org.crumbs.http.mvc.exception.*;
import org.crumbs.http.mvc.model.ResponseEntity;
import org.crumbs.json.JsonMapper;

import java.util.Map;


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
            if (handlerContext.preHandle(request, response)) {
                executeHandle(request, response);
            }

            handlerContext.postHandle(request, response);
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

    private void executeHandle(Request request, Response response) throws Exception {
        Map<HttpMethod, Handler> handlerMap = handlerContext.findHandler(request);
        if (handlerMap == null) {
            throw new NotFoundException("Could not find mapping for: " + request.getUrlPath());
        }
        Handler handler = handlerMap.get(request.getMethod());
        if (handler == null) {
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
    }


    private HandlerInvocationResult invokeHandler(Request request, Handler handler) throws Exception {
        return handler.invoke(request);
    }
}
