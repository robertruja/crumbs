package org.crumbs.client.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.crumbs.client.http.impl.sun.RequestBuilder;
import org.crumbs.client.http.model.HttpStatus;
import org.crumbs.client.http.model.Response;
import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.core.context.CrumbsApp;
import org.crumbs.core.context.CrumbsContext;
import org.crumbs.json.JsonMapper;
import org.crumbs.json.exception.JsonMarshalException;
import org.crumbs.json.exception.JsonUnmarshalException;
import org.crumbs.mvc.annotation.Handler;
import org.crumbs.mvc.annotation.HandlerRoot;
import org.crumbs.mvc.annotation.RequestBody;
import org.crumbs.mvc.common.model.HttpMethod;
import org.crumbs.mvc.common.model.Mime;
import org.crumbs.mvc.http.impl.sun.Server;
import org.crumbs.mvc.model.ResponseEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@CrumbsApplication
public class HttpClientTests {

    private static final String HANDLER_PATH = "/http-client-test";
    private static final String URL = "http://localhost:9009" + HANDLER_PATH;

    @HandlerRoot(HANDLER_PATH)
    static class TestController {

        @Handler(method = HttpMethod.GET, producesContent = Mime.TEXT_PLAIN)
        public String handle() {
            return "success get";
        }

        @Handler(method = HttpMethod.POST, producesContent = Mime.TEXT_PLAIN)
        public ResponseEntity<SomeResponsePayload> handlePost(@RequestBody SomeRequestPayload payload) {
            return ResponseEntity.status(org.crumbs.mvc.common.model.HttpStatus.OK)
                    .body(new SomeResponsePayload(payload.getAge() + payload.getName()));
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class SomeRequestPayload {
        String name;
        Integer age;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    static class SomeResponsePayload {
        String namaAge;
    }

    @Test
    public void shouldSendGetRequest() throws JsonUnmarshalException, JsonMarshalException, IllegalAccessException {

        CrumbsContext context = CrumbsApp.run(HttpClientTests.class);

        HttpClient client = context.getCrumbsWithInterface(HttpClient.class).get(0);

        Response response =
                client.doRequest(RequestBuilder.newRequest()
                        .url(URL)
                        .method(org.crumbs.client.http.model.HttpMethod.GET).build());

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("success get", new String(response.getBody()));

        JsonMapper mapper = new JsonMapper();

        response =
                client.doRequest(RequestBuilder.newRequest()
                        .payload(mapper.marshal(new SomeRequestPayload("aaa", 10)).getBytes())
                        .url(URL)
                        .method(org.crumbs.client.http.model.HttpMethod.POST).build());

        assertEquals(HttpStatus.OK, response.getStatus());

        assertEquals("10aaa", mapper.unmarshal(response.getBody(), SomeResponsePayload.class)
                .getNamaAge());


        Server server = context.getCrumb(Server.class);
        server.stop();

    }

}
