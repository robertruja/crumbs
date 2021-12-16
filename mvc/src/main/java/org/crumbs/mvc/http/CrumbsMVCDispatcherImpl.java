package org.crumbs.mvc.http;

import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.core.logging.Logger;
import org.crumbs.json.JsonMapper;
import org.crumbs.mvc.common.model.HttpStatus;
import org.crumbs.mvc.common.model.Mime;
import org.crumbs.mvc.context.HandlerContext;
import org.crumbs.mvc.context.HandlerInvocationResult;
import org.crumbs.mvc.exception.HandlerInvocationException;
import org.crumbs.mvc.exception.HandlerNotFoundException;
import org.crumbs.mvc.exception.HttpMethodNotAllowedException;
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
            if(!handlerContext.intercept(request, response)) {
                return;
            }
            HandlerInvocationResult result = handlerContext.invokeHandler(request);

            HttpStatus status;
            Object body;
            if(result.getReturnType().equals(String.class)) {
                status = HttpStatus.OK;
                body = result.getContent();
            } else if(result.getReturnType().equals(ResponseEntity.class)) {
                ResponseEntity<?> responseEntity = (ResponseEntity<?>)result.getContent();
                status = responseEntity.getStatus();
                body = jsonMapper.marshal(responseEntity.getBody());
            } else if(result.getReturnType() == null){
                status = HttpStatus.NO_CONTENT;
                body = null;
            } else {
                throw new Exception("Return type not supported");
            }

            response.setStatus(status);
            response.setMime(result.getMime());
            response.setBody(body == null ? new byte[]{} : body.toString().getBytes());

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
        logger.debug("Handeled request {} {} in {} ms", request.getMethod(), request.getUrlPath(), end);
    }
}
