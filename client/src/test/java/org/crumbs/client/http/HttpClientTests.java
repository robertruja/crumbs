package org.crumbs.client.http;

import org.crumbs.client.http.impl.sun.RequestBuilder;
import org.crumbs.client.http.model.HttpStatus;
import org.crumbs.client.http.model.Response;
import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.core.context.CrumbsApp;
import org.crumbs.core.context.CrumbsContext;
import org.crumbs.mvc.annotation.Handler;
import org.crumbs.mvc.annotation.HandlerRoot;
import org.crumbs.mvc.common.model.HttpMethod;
import org.crumbs.mvc.common.model.Mime;
import org.crumbs.mvc.http.impl.sun.Server;
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
            return "success";
        }
    }

    @Test
    public void shouldSendGetRequest() {

        CrumbsContext context = CrumbsApp.run(HttpClientTests.class);

        HttpClient client = context.getCrumbsWithInterface(HttpClient.class).get(0);

        Response response =
                client.doRequest(RequestBuilder.newRequest()
                        .url(URL)
                        .method(org.crumbs.client.http.model.HttpMethod.GET).build());

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("success", new String(response.getBody()));


        Server server = context.getCrumb(Server.class);
        server.stop();

    }

}
