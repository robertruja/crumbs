package org.crumbs.mvc.handler;

import org.crumbs.mvc.annotation.*;
import org.crumbs.mvc.common.model.HttpMethod;
import org.crumbs.mvc.common.model.HttpStatus;
import org.crumbs.mvc.common.model.Mime;
import org.crumbs.mvc.model.PathParamResponse;
import org.crumbs.mvc.model.ResponseEntity;
import org.crumbs.mvc.model.SimpleRequestModel;
import org.crumbs.mvc.model.SimpleResponseModel;

import java.util.Map;

@HandlerRoot("/test-handler")
public class TestHandler {

    @Handler(value = "/test-get", producesContent = Mime.TEXT_PLAIN)
    public String testGet() {
        return "hello from get";
    }

    @Handler(value = "/test-post", method = HttpMethod.POST)
    public ResponseEntity<?> someRandomHandlerWithPost(@RequestBody SimpleRequestModel requestBody) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SimpleResponseModel("Got name: " + requestBody.getName() + ", age: " + requestBody.getAge(), null, 0, null));
    }

    @Handler(value = "/test-get", method = HttpMethod.PUT)
    public ResponseEntity<?> somePutWithParamsAndBody(@RequestParam String stringParam, @RequestParam("theInt") Integer intParam,

                                                      @RequestBody Map<String, Object> someModel,
                                                      @RequestParam Long nullableLongParam) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SimpleResponseModel(stringParam, nullableLongParam, intParam, someModel));
    }

    @Handler(value = "/test/{some-string}/test/{someInt}/some")
    public ResponseEntity<?> methodWithPathParams(@PathVariable("some-string") String stringParam, @PathVariable Integer someInt) {
        String response = "" + stringParam + " and " + someInt;
        return ResponseEntity.status(HttpStatus.OK)
                .body(new PathParamResponse(response));
    }
}
