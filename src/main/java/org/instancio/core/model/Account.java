package org.instancio.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Account {

    private String number;

    private Integer order;
}
