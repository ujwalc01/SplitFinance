package com.splitfinance.reporting.service;

import com.splitfinance.expense.entity.Expense;
import com.splitfinance.expense.repository.ExpenseRepository;
import com.splitfinance.reporting.dto.ExpenseReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ReportingServiceImpl implements ReportingService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ReportingServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    
    @Override
    public ExpenseReportDTO generateExpenseReportForGroup(Long groupId) {
        // Retrieve all expenses for the given group.
        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        
        // Sum up the total expense amount.
        BigDecimal totalExpense = expenses.stream()
                 .map(Expense::getTotalAmount)
                 .reduce(BigDecimal.ZERO, BigDecimal::add);
                 
        int count = expenses.size();
        BigDecimal averageExpense = count > 0 
                 ? totalExpense.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) 
                 : BigDecimal.ZERO;
        
        ExpenseReportDTO report = new ExpenseReportDTO();
        report.setGroupId(groupId);
        report.setTotalExpense(totalExpense);
        report.setExpenseCount(count);
        report.setAverageExpense(averageExpense);
        
        return report;
    }
}