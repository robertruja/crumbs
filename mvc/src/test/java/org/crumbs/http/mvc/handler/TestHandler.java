package org.crumbs.http.mvc.handler;

import org.crumbs.http.mvc.annotation.*;
import org.crumbs.http.common.model.HttpMethod;
import org.crumbs.http.common.model.HttpStatus;
import org.crumbs.http.common.model.Mime;
import org.crumbs.http.mvc.model.SimpleRequestModel;
import org.crumbs.http.mvc.model.PathParamResponse;
import org.crumbs.http.mvc.model.ResponseEntity;
import org.crumbs.http.mvc.model.SimpleResponseModel;

import java.util.Map;

@HandlerRoot("/test-handler")
public class TestHandler {

    @Handler(mapping = "/test-get", producesContent = Mime.TEXT_PLAIN)
    public String testGet() {
        return "hello from get";
    }

    @Handler(mapping = "/test-post", method = HttpMethod.POST)
    public ResponseEntity<?> someRandomHandlerWithPost(@RequestBody SimpleRequestModel requestBody) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SimpleResponseModel("Got name: " + requestBody.getName() + ", age: " + requestBody.getAge(), null, 0, null));
    }

    @Handler(mapping = "/test-get", method = HttpMethod.PUT)
    public ResponseEntity<?> somePutWithParamsAndBody(@RequestParam String stringParam, @RequestParam("theInt") Integer intParam,

                                                      @RequestBody Map<String, Object> someModel,
                                                      @RequestParam Long nullableLongParam) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SimpleResponseModel(stringParam, nullableLongParam, intParam, someModel));
    }

    @Handler(mapping = "/test/{some-string}/test/{someInt}/some")
    public ResponseEntity<?> methodWithPathParams(@PathVariable("some-string") String stringParam, @PathVariable Integer someInt) {
        String response = "" + stringParam + " and " + someInt;
        return ResponseEntity.status(HttpStatus.OK)
                .body(new PathParamResponse(response));
    }

    @Handler(mapping = "/test/params")
    public String primitiveParams(@RequestParam(required = true) int someParam) {
        return "value is: " + someParam;
    }
}
