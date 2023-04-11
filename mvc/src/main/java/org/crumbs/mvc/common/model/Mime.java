package org.crumbs.mvc.common.model;

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
    DNS_MESSAGE("application/dns-message"),
    ZIP("application/zip");

    private String value;

    Mime(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
