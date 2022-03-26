package org.crumbs.client;

import org.crumbs.client.http.model.HttpMethod;
import org.crumbs.client.http.model.HttpStatus;
import org.crumbs.client.rest.RestClient;
import org.crumbs.client.rest.model.RequestEntity;
import org.crumbs.client.rest.model.ResponseEntity;
import org.crumbs.core.annotation.Crumb;
import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.core.context.CrumbsApp;
import org.crumbs.core.context.CrumbsContext;
import org.crumbs.mvc.http.impl.sun.Server;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@CrumbsApplication
@Crumb
public class TestRestClient {

    private static String BASE_URL = "http://localhost:9009";

//    @CrumbRef
//    private RestClient client;

    @CrumbRef
    private Server server;

    @Test
    public void shouldReceiveSuccessfulResponse() {
        CrumbsContext context = CrumbsApp.run(TestRestClient.class);

        RestClient client = context.getCrumbsWithInterface(RestClient.class).get(0);
        Server server = context.getCrumb(Server.class);


        ResponseEntity<TestResponseBody> response =
                client.doRequest(RequestEntity.builder().method(HttpMethod.POST)
                                .url(BASE_URL + "/test/handle")
                                .body(TestRequestBody.builder()
                                        .age(10)
                                        .name("gigi")
                                )
                                .build(),
                        TestResponseBody.class);

        TestResponseBody responseBody = response.getBody();

        assertEquals(HttpStatus.MULTI_STATUS, response.getStatus());
        assertEquals(11, responseBody.getAge());
        assertEquals("gigi updated", responseBody.getName());
        assertNotNull(response.getResponse());
        assertEquals(121233L, responseBody.getId());

        server.stop();
    }
}
