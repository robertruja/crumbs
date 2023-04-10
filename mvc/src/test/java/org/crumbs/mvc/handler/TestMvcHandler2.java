package org.crumbs.mvc.handler;


import org.crumbs.mvc.annotation.Handler;
import org.crumbs.mvc.annotation.HandlerRoot;
import org.crumbs.mvc.annotation.PathVariable;

import static org.crumbs.mvc.common.model.HttpMethod.*;


@HandlerRoot("/tree-test2")
public class TestMvcHandler2 {

    @Handler(method = PUT)
    public String firstLevel() {
        return "put";
    }

    @Handler(method = GET)
    public String firstLevelGET() {
        return "get";
    }

    @Handler(mapping = "/{aaa}", method = PUT)
    public String firstLevelPut(@PathVariable String aaa) {
        return "put_" + aaa;
    }

    @Handler(mapping = "/{bbb}", method = POST)
    public String firstLevelPost(@PathVariable("bbb") String aaa) {
        return "post_" + aaa;
    }

    @Handler(mapping = "/first", method = DELETE)
    public String firstLevelDELETE() {
        return "OK";
    }

    @Handler(mapping = "/first/second", method = DELETE)
    public String firstLevelSecond() {
        return "first_second";
    }

    @Handler(mapping = "/first/second/{bbb}", method = DELETE)
    public String firstLevelSecondBBB() {
        return "first_second_bbb";
    }

    @Handler(mapping = "/first/{bbb}/third", method = DELETE)
    public String firstLevelThirdBBB() {
        return "bbb_third";
    }

    @Handler(mapping = "/first/{ccc}/third", method = PUT)
    public String firstLevelThirdCCC() {
        return "ccc_third";
    }

    @Handler(mapping = "/first/second/third", method = POST)
    public String firstLevelSecondThird() {
        return "firstsecondthird";
    }

}
