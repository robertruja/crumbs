package org.crumbs.testsupport.mvc;

import org.crumbs.http.client.http.model.HttpMethod;
import org.crumbs.http.client.http.model.HttpStatus;
import org.crumbs.http.client.rest.RestClient;
import org.crumbs.http.client.rest.model.RequestEntity;
import org.crumbs.http.client.rest.model.ResponseEntity;
import org.crumbs.core.annotation.CrumbRef;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MvcTest(port = 9009)
public class TestServerInit {

    private static final String URL = "http://localhost:9009/test";

    @CrumbRef
    private RestClient restClient;


    @Test
    public void shouldStartAndStopServerAfterTest() {

        ResponseEntity<String> respone =
                restClient.doRequest(RequestEntity.builder().url(URL).method(HttpMethod.GET).build(), String.class);

        Assertions.assertEquals(HttpStatus.OK, respone.getStatus());
        Assertions.assertEquals("success", respone.getBody());
    }
}
