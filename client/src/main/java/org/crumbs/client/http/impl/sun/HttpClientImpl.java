package org.crumbs.client.http.impl.sun;

import org.crumbs.client.exception.ConnectException;
import org.crumbs.client.http.HttpClient;
import org.crumbs.client.http.model.HttpMethod;
import org.crumbs.client.http.model.HttpStatus;
import org.crumbs.client.http.model.Request;
import org.crumbs.client.http.model.Response;
import org.crumbs.core.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpClientImpl implements HttpClient {
    @Override
    public Response doRequest(Request request) {
        try {


            URL url = request.getUrl();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            setHeaders(connection, request.getHeaders());

            HttpMethod httpMethod = request.getMethod();
            if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT) {
                connection.setDoInput(true);
                OutputStream out = connection.getOutputStream();
                if (request.getPayload().length != 0) {
                    IOUtils.writeToOutputStream(request.getPayload(), out);
                } else if (request.getPayloadInput() != null) {
                    IOUtils.writeIO(request.getPayloadInput(), out);
                }
            }
            connection.setRequestMethod(httpMethod.name());


            int responseCode = connection.getResponseCode();
            Map<String, List<String>> responseHeaders = connection.getHeaderFields();
            InputStream responsePayload = connection.getInputStream();
            ResponseImpl response = new ResponseImpl();
            response.setStatusCode(HttpStatus.fromIntCode(responseCode));
            response.setHeaders(Collections.unmodifiableMap(responseHeaders));
            response.setPayload(responsePayload);

            return response;
        } catch (IOException e) {
            throw new ConnectException("Unable to connect to url: " + request.getUrlPath(), e);
        }
    }

    private void setHeaders(HttpURLConnection connection, Map<String, List<String>> headers) {
        headers.forEach((k, v) -> connection.setRequestProperty(k, fromHeaderList(v)));
    }

    private String fromHeaderList(List<String> header) {
        return header.toString().substring(1, header.size());
    }
}
