package com.splitfinance.reporting.dto;

import java.math.BigDecimal;

public class ExpenseReportDTO {
    
    private Long groupId;
    private BigDecimal totalExpense;
    private int expenseCount;
    private BigDecimal averageExpense;
    
    public ExpenseReportDTO() {}
    
    // Getters and setters
    
    public Long getGroupId() {
        return groupId;
    }
    
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    
    public BigDecimal getTotalExpense() {
        return totalExpense;
    }
    
    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }
    
    public int getExpenseCount() {
        return expenseCount;
    }
    
    public void setExpenseCount(int expenseCount) {
        this.expenseCount = expenseCount;
    }
    
    public BigDecimal getAverageExpense() {
        return averageExpense;
    }
    
    public void setAverageExpense(BigDecimal averageExpense) {
        this.averageExpense = averageExpense;
    }
}
