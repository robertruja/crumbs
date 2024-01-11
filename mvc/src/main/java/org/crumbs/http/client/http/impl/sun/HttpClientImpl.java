package org.crumbs.http.client.http.impl.sun;

import org.crumbs.http.client.http.model.HttpStatus;
import org.crumbs.http.client.exception.ConnectException;
import org.crumbs.http.client.http.HttpClient;
import org.crumbs.http.client.http.model.Request;
import org.crumbs.http.client.http.model.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class HttpClientImpl implements HttpClient {

    java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

    @Override
    public Response doRequest(Request request) {
        try {

            HttpRequest.BodyPublisher publisher;

            if (request.getPayload().length != 0) {
                publisher = HttpRequest.BodyPublishers.ofByteArray(request.getPayload());
            } else if (request.getPayloadInput() != null) {
                publisher = HttpRequest.BodyPublishers.ofInputStream(request::getPayloadInput);
            } else {
                publisher = HttpRequest.BodyPublishers.noBody();
            }

            HttpRequest.Builder builder = HttpRequest.newBuilder().uri(request.getUrl().toURI())
                    .method(request.getMethod().name(), publisher);

            setHeaders(builder, request.getHeaders());

            HttpResponse<InputStream> httpResponse =
                    client.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());

            org.crumbs.http.client.http.impl.sun.ResponseImpl response = new ResponseImpl();
            response.setStatusCode(HttpStatus.fromIntCode(httpResponse.statusCode()));
            response.setHeaders(httpResponse.headers().map());
            response.setPayload(determinePaylodByEncoding(httpResponse));

            return response;
        } catch (InterruptedException | IOException | URISyntaxException e) {
            throw new ConnectException("An error occurred when sending request to: " + request.getUrlPath(), e);
        }
    }

    private InputStream determinePaylodByEncoding(HttpResponse<InputStream> response) throws IOException {
        String encoding = response.headers().firstValue("Content-Encoding").orElse("");
        switch (encoding) {
            case "gzip":
                return new GZIPInputStream(response.body());
            case "":
                return response.body();
            default:
                throw new UnsupportedEncodingException("Unsupported encoding: " + encoding);
        }
    }


    private void setHeaders(HttpRequest.Builder builder, Map<String, List<String>> headers) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            for (String value : entry.getValue()) {
                builder.header(entry.getKey(), value);
            }
        }
    }
}
