package com.splitfinance.expense.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ExpenseSplitDTO {
    private String participant;
    // For custom split, specify the amount; for equal split this can be null.
    private BigDecimal splitAmount;
}

