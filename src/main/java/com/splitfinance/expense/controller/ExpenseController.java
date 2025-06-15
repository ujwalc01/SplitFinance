package com.splitfinance.expense.controller;

import com.splitfinance.expense.dto.ExpenseCreateDTO;
import com.splitfinance.expense.dto.ExpenseResponseDTO;
import com.splitfinance.expense.dto.ExpenseUpdateDTO;
import com.splitfinance.expense.service.ExpenseService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(@Valid @RequestBody ExpenseCreateDTO expenseCreateDTO,
                                                            Authentication authentication) {
        // Use the authenticated user's username as the payer.
        String payer = authentication.getName();
        ExpenseResponseDTO createdExpense = expenseService.createExpense(payer, expenseCreateDTO);
        return ResponseEntity.ok(createdExpense);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(Authentication authentication) {
        String payer = authentication.getName();
        List<ExpenseResponseDTO> expenses = expenseService.getExpensesForUser(payer);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable Long id, Authentication authentication) {
        String payer = authentication.getName();
        ExpenseResponseDTO expense = expenseService.getExpenseById(id, payer);
        return ResponseEntity.ok(expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(@PathVariable Long id,
                                                            @Valid @RequestBody ExpenseUpdateDTO expenseUpdateDTO,
                                                            Authentication authentication) {
        String payer = authentication.getName();
        ExpenseResponseDTO updatedExpense = expenseService.updateExpense(id, payer, expenseUpdateDTO);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id, Authentication authentication) {
        String payer = authentication.getName();
        expenseService.deleteExpense(id, payer);
        return ResponseEntity.ok("Expense deleted successfully");
    }
}
