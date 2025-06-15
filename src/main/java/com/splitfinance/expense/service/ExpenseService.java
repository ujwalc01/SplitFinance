package com.splitfinance.expense.service;

import java.util.List;

import com.splitfinance.expense.dto.ExpenseCreateDTO;
import com.splitfinance.expense.dto.ExpenseResponseDTO;
import com.splitfinance.expense.dto.ExpenseUpdateDTO;

public interface ExpenseService {
    ExpenseResponseDTO createExpense(String payer, ExpenseCreateDTO expenseCreateDTO);
    ExpenseResponseDTO updateExpense(Long id, String payer, ExpenseUpdateDTO expenseUpdateDTO);
    void deleteExpense(Long id, String payer);
    ExpenseResponseDTO getExpenseById(Long id, String payer);
    List<ExpenseResponseDTO> getExpensesForUser(String payer);
}
