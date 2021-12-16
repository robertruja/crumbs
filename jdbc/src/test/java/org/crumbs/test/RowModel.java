package org.crumbs.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowModel {

    private int id;
    private String name;
    private int age;
    private long eventTime;

}
