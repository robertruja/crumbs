package org.crumbs.mvc.handler;

import org.crumbs.client.http.model.HttpMethod;
import org.crumbs.client.http.model.HttpStatus;
import org.crumbs.client.rest.RestClient;
import org.crumbs.client.rest.model.RequestEntity;
import org.crumbs.client.rest.model.ResponseEntity;
import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.core.context.CrumbsApp;
import org.crumbs.core.context.CrumbsContext;

import org.crumbs.mvc.http.impl.sun.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@CrumbsApplication
public class PathFindTests {
    private static final String BASE_URL = "http://localhost:8008";


    static RestClient client;
    static Server server;

    @BeforeAll
    public static void init() {
        CrumbsContext context = CrumbsApp.run(PathFindTests.class);

        client = context.getCrumbsWithInterface(RestClient.class).get(0);
        server = context.getCrumb(Server.class);
    }

    @Test
    public void shouldReturnReturnOk() {

        perform("/tree-test2", HttpMethod.PUT, "put");
        perform("/tree-test2", HttpMethod.GET, "get");
        perform("/tree-test2/test", HttpMethod.PUT, "put_test");
        perform("/tree-test2/test1", HttpMethod.POST, "post_test1");
        perform("/tree-test2/first", HttpMethod.DELETE, "OK");
        perform("/tree-test2/first/second", HttpMethod.DELETE, "first_second");
        perform("/tree-test2/first/second/test", HttpMethod.DELETE, "first_second_bbb");
        perform("/tree-test2/first/fourth/third", HttpMethod.DELETE, "bbb_third");
        perform("/tree-test2/first/fourth/third", HttpMethod.PUT, "ccc_third");
        perform("/tree-test2/first/second/third", HttpMethod.POST, "firstsecondthird");

        perform("/tree-test1", HttpMethod.PUT, "OK");
        perform("/tree-test1", HttpMethod.GET, "OK");
        perform("/tree-test1/test", HttpMethod.PUT, "test");
        perform("/tree-test1/first", HttpMethod.DELETE, "OK");
        perform("/tree-test1/first/second", HttpMethod.DELETE, "OK");
        perform("/tree-test1/first/second/test", HttpMethod.DELETE, "first_second_bbb");
        perform("/tree-test1/first/1234/third", HttpMethod.DELETE, "bbb_third_1234");
        perform("/tree-test1/first/some/third", HttpMethod.PUT, "ccc_third");
        perform("/tree-test1/first/second/third", HttpMethod.POST, "OK");

    }

    @Test
    public void shouldReturnNotFound() {
        ResponseEntity<String> response =
                client.doRequest(RequestEntity.builder().method(HttpMethod.PUT)
                                .url(BASE_URL + "/tree-test3/some/123")
                                .build(),
                        String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    public void shouldReturnMethodNotAllowed() {
        ResponseEntity<String> response =
                client.doRequest(RequestEntity.builder().method(HttpMethod.PUT)
                                .url(BASE_URL + "/tree-test2/first/second/third")
                                .build(),
                        String.class);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatus());
    }

    private void perform(String path, HttpMethod method, String result) {
        ResponseEntity<String> response =
                client.doRequest(RequestEntity.builder().method(method)
                                .url(BASE_URL + path)
                                .build(),
                        String.class);

        String responseBody = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(result, responseBody);
    }

    @AfterAll
    public static void destroy() {
        server.stop();
    }
}
