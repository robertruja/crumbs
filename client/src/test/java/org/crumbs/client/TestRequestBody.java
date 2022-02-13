package org.crumbs.client;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
public class TestRequestBody {
    private int age;
    private String name;
}
