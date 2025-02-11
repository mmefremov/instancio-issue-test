package org.instancio.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@ToString
public class Due {

    private BigDecimal amount;

    private BigDecimal amountLocal;

    private LocalDate date;
}
