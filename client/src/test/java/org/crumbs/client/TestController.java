package org.crumbs.client;

import org.crumbs.core.logging.Logger;
import org.crumbs.mvc.annotation.Handler;
import org.crumbs.mvc.annotation.HandlerRoot;
import org.crumbs.mvc.annotation.RequestBody;
import org.crumbs.mvc.common.model.HttpMethod;
import org.crumbs.mvc.common.model.HttpStatus;
import org.crumbs.mvc.model.ResponseEntity;

@HandlerRoot("/test")
public class TestController {

    private Logger logger = Logger.getLogger(TestController.class);

    @Handler(method = HttpMethod.POST, mapping = "/handle")
    public ResponseEntity<TestResponseBody> handle(@RequestBody TestRequestBody body) {
        logger.info("Received body: {}", body);

        TestResponseBody responseBody = TestResponseBody.builder()
                .id(121233L)
                .age(body.getAge() + 1)
                .name(body.getName() + " updated")
                .build();

        return ResponseEntity.status(HttpStatus.MULTI_STATUS)
                .body(responseBody);
    }
}
