package org.instancio.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class LoanRequest {

    private List<Account> accounts;

    private List<Due> schedule;
}
