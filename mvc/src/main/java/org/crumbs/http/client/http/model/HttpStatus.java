package org.crumbs.http.client.http.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum HttpStatus {
    _NULL(0),
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),
    MULTI_STATUS(207),
    MOVED_PERMANENTLY(301),
    FOUND(302),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    NOT_ACCEPTABLE(406),
    REQUEST_TIMEOUT(408),
    CONFLICT(409),
    CONTENT_TOO_LARGE(413),
    UNSUPPORTED_MEDIA_TYPE(415),
    INTERNAL_SERVER_ERROR(500),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504);

    private static Map<Integer, HttpStatus> codes = Arrays.stream(HttpStatus.values()).collect(Collectors.toMap(
            HttpStatus::getCode, httpStatus -> httpStatus
    ));
    private int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public static HttpStatus fromIntCode(int responseCode) {
        return codes.getOrDefault(responseCode, _NULL);
    }

    public int getCode() {
        return code;
    }
}
