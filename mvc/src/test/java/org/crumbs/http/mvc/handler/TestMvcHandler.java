package org.crumbs.http.mvc.handler;


import org.crumbs.http.mvc.annotation.Handler;
import org.crumbs.http.mvc.annotation.HandlerRoot;
import org.crumbs.http.mvc.annotation.PathVariable;

import static org.crumbs.http.common.model.HttpMethod.*;


@HandlerRoot("/tree-test1")
public class TestMvcHandler {

    @Handler(method = PUT)
    public String firstLevel() {
        return "OK";
    }

    @Handler(method = GET)
    public String firstLevelGET() {
        return "OK";
    }

    @Handler(mapping = "/{aaa}", method = PUT)
    public String firstLevel(@PathVariable("aaa") String aaa) {
        return aaa;
    }

    @Handler(mapping = "/first", method = DELETE)
    public String firstLevelDELETE() {
        return "OK";
    }

    @Handler(mapping = "/first/second", method = DELETE)
    public String firstLevelSecond() {
        return "OK";
    }

    @Handler(mapping = "/first/second/{bbb}", method = DELETE)
    public String firstLevelSecondBBB() {
        return "first_second_bbb";
    }

    @Handler(mapping = "/first/{val}/third", method = DELETE)
    public String firstLevelThirdBBB(@PathVariable int val) {
        return "bbb_third_" + val;
    }

    @Handler(mapping = "/first/{ccc}/third", method = PUT)
    public String firstLevelThirdCCC() {
        return "ccc_third";
    }

    @Handler(mapping = "/first/second/third", method = POST)
    public String firstLevelSecondThird() {
        return "OK";
    }

}
