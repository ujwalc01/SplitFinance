package com.splitfinance.expense.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExpenseResponseDTO {
    private Long id;
    private String description;
    private BigDecimal totalAmount;
    private String payer;
    private LocalDateTime createdAt;
    private List<ExpenseSplitDTO> splits;
}
