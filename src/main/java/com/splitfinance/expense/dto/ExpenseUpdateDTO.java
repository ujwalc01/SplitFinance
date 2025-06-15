package com.splitfinance.expense.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ExpenseUpdateDTO {
    private String description;
    private BigDecimal totalAmount;
    private List<ExpenseSplitDTO> splits;
    private List<String> participants;
}
