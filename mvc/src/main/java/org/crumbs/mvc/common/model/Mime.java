package org.crumbs.mvc.common.model;

public enum Mime {
    APPLICATION_JSON("application/json"),
    TEXT_HTML("text/html"),
    TEXT_PLAIN("text/plain");

    private String value;

    Mime(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
