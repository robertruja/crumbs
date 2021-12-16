package org.crumbs.mvc.http.impl.sun;

import com.sun.net.httpserver.HttpExchange;
import org.crumbs.core.util.IOUtil;
import org.crumbs.mvc.common.model.HttpMethod;
import org.crumbs.mvc.http.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestImpl implements Request {

    private HttpMethod method;
    private String urlPath;
    private HttpExchange exchange;
    private Map<String, Object> attributes = new HashMap<>();
    private Map<String, List<String>> headers = new HashMap<>();
    private Map<String, String> queryParams = new HashMap<>();
    private Map<String, String> pathVariables = new HashMap<>();

    private RequestImpl() {
    }

    public static Request from(HttpExchange exchange) {

        RequestImpl request = new RequestImpl();
        request.exchange = exchange;
        request.method = HttpMethod.valueOf(exchange.getRequestMethod());
        request.urlPath = exchange.getRequestURI().getPath();
        request.readQuery(exchange.getRequestURI().getQuery());
        return request;
    }

    public byte[] getBody() {
        return IOUtil.readInputStream(exchange.getRequestBody());
    }

    public String getBodyAsString(Charset charset) {
        return new String(getBody(), charset);
    }

    @Override
    public String getPathVarialbe(String name) {
        return pathVariables.get(name);
    }

    private void readQuery(String qs) {
        if (qs == null)
            return;
        int last = 0, next, l = qs.length();
        while (last < l) {
            next = qs.indexOf('&', last);
            if (next == -1)
                next = l;
            if (next > last) {
                int eqPos = qs.indexOf('=', last);
                try {
                    if (eqPos < 0 || eqPos > next)
                        queryParams.put(URLDecoder.decode(qs.substring(last, next), "utf-8"), "");
                    else
                        queryParams.put(URLDecoder.decode(qs.substring(last, eqPos), "utf-8"),
                                URLDecoder.decode(qs.substring(eqPos + 1, next), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            last = next + 1;
        }
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public HttpExchange getExchange() {
        return exchange;
    }

    @Override
    public void setPathVariable(String name, String value) {
        pathVariables.put(name, value);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }
}
