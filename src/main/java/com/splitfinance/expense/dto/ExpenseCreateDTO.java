package com.splitfinance.expense.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ExpenseCreateDTO {
    private String description;
    private BigDecimal totalAmount;
    
    // Optional: If provided, use these custom splits
    private List<ExpenseSplitDTO> splits;
    
    // Otherwise, if provided, split equally among these participant usernames
    private List<String> participants;
}
