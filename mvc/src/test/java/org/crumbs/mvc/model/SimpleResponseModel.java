package org.crumbs.mvc.model;

import java.util.Map;

public class SimpleResponseModel {
    private String response;
    private Long someLong;
    private Integer someInt;
    private Map<String, Object> requestModel;

    public SimpleResponseModel(String response, Long someLong, Integer someInt, Map<String, Object> requestModel) {
        this.response = response;
        this.someLong = someLong;
        this.someInt = someInt;
        this.requestModel = requestModel;
    }
}
