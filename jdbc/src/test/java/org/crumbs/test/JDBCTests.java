package org.crumbs.test;

import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.core.context.CrumbsApp;
import org.crumbs.core.context.CrumbsContext;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@Ignore
@CrumbsApplication
public class JDBCTests {

    @Test
    public void shouldInsertAndRetrieveData() {

        CrumbsContext context = CrumbsApp.run(JDBCTests.class);
        TestDao testDao = context.getCrumb(TestDao.class);

        RowModel rowModel1 = RowModel.builder()
                .id(1)
                .name("first")
                .age(10)
                .eventTime(System.currentTimeMillis())
                .build();

        testDao.insertRow(rowModel1);

        RowModel rowModel2 = RowModel.builder()
                .id(2)
                .name("second")
                .age(15)
                .eventTime(System.currentTimeMillis())
                .build();

        testDao.insertRow(rowModel2);


        RowModel rowModel3 = RowModel.builder()
                .id(3)
                .name("third")
                .age(20)
                .eventTime(System.currentTimeMillis())
                .build();

        testDao.insertRow(rowModel3);

        List<RowModel> results = testDao.retrieveRowModels(16);

        assertEquals(3, results.size());
        RowModel res1 = results.stream().filter(res -> res.getId() == 1).findFirst().get();
        RowModel res2 = results.stream().filter(res -> res.getId() == 2).findFirst().get();
        RowModel res3 = results.stream().filter(res -> res.getId() == 3).findFirst().get();

        assertEquals("first", res1.getName());
        assertEquals("second", res2.getName());
        assertEquals("third", res3.getName());

        assertEquals(10, res1.getAge());
        assertEquals(15, res2.getAge());
        assertEquals(20, res3.getAge());
    }

    @Test
    public void shouldInsertBatch() {
        CrumbsContext context = CrumbsApp.run(JDBCTests.class);
        TestDao testDao = context.getCrumb(TestDao.class);
        RowModel rowModel1 = RowModel.builder()
                .id(1)
                .name("first")
                .age(10)
                .eventTime(System.currentTimeMillis())
                .build();

        RowModel rowModel2 = RowModel.builder()
                .id(2)
                .name("second")
                .age(15)
                .eventTime(System.currentTimeMillis())
                .build();

        RowModel rowModel3 = RowModel.builder()
                .id(3)
                .name("third")
                .age(20)
                .eventTime(System.currentTimeMillis())
                .build();

        testDao.insertBatch(Arrays.asList(rowModel1, rowModel2, rowModel3));

        List<RowModel> results = testDao.retrieveRowModels(16);

        assertEquals(3, results.size());
        RowModel res1 = results.stream().filter(res -> res.getId() == 1).findFirst().get();
        RowModel res2 = results.stream().filter(res -> res.getId() == 2).findFirst().get();
        RowModel res3 = results.stream().filter(res -> res.getId() == 3).findFirst().get();

        assertEquals("first", res1.getName());
        assertEquals("second", res2.getName());
        assertEquals("third", res3.getName());

        assertEquals(10, res1.getAge());
        assertEquals(15, res2.getAge());
        assertEquals(20, res3.getAge());
    }
}
