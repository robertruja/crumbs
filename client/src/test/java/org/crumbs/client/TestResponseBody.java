package org.crumbs.client;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TestResponseBody {
    private long id;
    private String name;
    private int age;
}
