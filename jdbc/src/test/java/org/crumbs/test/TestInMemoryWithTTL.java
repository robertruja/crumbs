package org.crumbs.test;

import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.core.context.CrumbsApp;
import org.crumbs.core.context.CrumbsContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Disabled
@CrumbsApplication
public class TestInMemoryWithTTL {
    static {
        System.setProperty("log.level", "DEBUG");
    }

    @Test
    public void shouldCloseConnectionAfterTTL() throws InterruptedException {
        CrumbsContext context = CrumbsApp.run(TestInMemoryWithTTL.class);
        TestDao testDao = context.getCrumb(TestDao.class);

        RowModel rowModel1 = RowModel.builder()
                .id(1)
                .name("first")
                .age(10)
                .eventTime(System.currentTimeMillis())
                .build();

        testDao.insertRow(rowModel1);

        List<RowModel> results = testDao.retrieveRowModels(16);

        Assertions.assertEquals(1, results.size());
        System.out.println(results);

        for (int i = 0; i < 10; i++) {
            TimeUnit.SECONDS.sleep(2);

            results = testDao.retrieveRowModels(16);

            Assertions.assertEquals(1, results.size());
            System.out.println(results);
        }

        TimeUnit.SECONDS.sleep(4);
        results = testDao.retrieveRowModels(16);
        Assertions.assertEquals(1, results.size());
        System.out.println(results);

        TimeUnit.SECONDS.sleep(6);
        results = testDao.retrieveRowModels(16);
        Assertions.assertEquals(1, results.size());
        System.out.println(results);
    }
}
