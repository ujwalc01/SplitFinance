package com.splitfinance.expense.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.splitfinance.expense.entity.Expense;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Retrieve expenses where the given username is the payer.
    List<Expense> findByPayer(String payer);
    List<Expense> findByGroupId(Long groupId);
}
