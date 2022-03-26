package org.crumbs.client.http.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Mime {
    APPLICATION_JSON("application/json"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    TEXT_HTML("text/html"),
    XHTML("application/xhtml+xml"),
    XML("application/xml"),
    TEXT_PLAIN("text/plain"),
    TEXT_CSS("text/css"),
    ICO("image/vnd.microsoft.icon"),
    BMP("image/bmp"),
    GIF("image/gif"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    SVG("image/svg+xml"),
    PDF("application/pdf"),
    TEXT_JAVASCRIPT("text/javascript"),
    ZIP("application/zip");

    private static Map<String, Mime> values =
            Arrays.stream(Mime.values()).collect(Collectors.toMap(Mime::getValue, mime -> mime));
    private String value;

    Mime(String value) {
        this.value = value;
    }

    public static Mime fromValue(String value) {
        return values.get(value);
    }

    public String getValue() {
        return value;
    }
}
