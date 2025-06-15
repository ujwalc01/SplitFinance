package com.splitfinance.settlement.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SettlementTransactionDTO {
    // "from" denotes the user paying money
    private String from;
    // "to" denotes the user receiving money
    private String to;
    // Transaction amount
    private BigDecimal amount;
}
