package com.splitfinance.expense.service;

import com.splitfinance.expense.dto.ExpenseCreateDTO;
import com.splitfinance.expense.dto.ExpenseResponseDTO;
import com.splitfinance.expense.dto.ExpenseSplitDTO;
import com.splitfinance.expense.dto.ExpenseUpdateDTO;
import com.splitfinance.expense.entity.Expense;
import com.splitfinance.expense.entity.ExpenseSplit;
import com.splitfinance.expense.repository.ExpenseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public ExpenseResponseDTO createExpense(String payer, ExpenseCreateDTO dto) {
        Expense expense = new Expense();
        expense.setDescription(dto.getDescription());
        expense.setTotalAmount(dto.getTotalAmount());
        expense.setCreatedAt(LocalDateTime.now());
        expense.setPayer(payer);
        
        List<ExpenseSplit> splits = new ArrayList<>();
        
        if (dto.getSplits() != null && !dto.getSplits().isEmpty()) {
            // Use custom splits provided by the caller.
            for (ExpenseSplitDTO splitDTO : dto.getSplits()) {
                ExpenseSplit split = new ExpenseSplit();
                split.setParticipant(splitDTO.getParticipant());
                split.setSplitAmount(splitDTO.getSplitAmount());
                split.setExpense(expense);
                splits.add(split);
            }
        } else if (dto.getParticipants() != null && !dto.getParticipants().isEmpty()) {
            // Calculate equal split among provided participants.
            int count = dto.getParticipants().size();
            BigDecimal share = dto.getTotalAmount().divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP);
            for (String participant : dto.getParticipants()) {
                ExpenseSplit split = new ExpenseSplit();
                split.setParticipant(participant);
                split.setSplitAmount(share);
                split.setExpense(expense);
                splits.add(split);
            }
        }
        expense.setSplits(splits);
        
        Expense savedExpense = expenseRepository.save(expense);
        return mapToExpenseResponseDTO(savedExpense);
    }

    @Override
    public ExpenseResponseDTO updateExpense(Long id, String payer, ExpenseUpdateDTO dto) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!expense.getPayer().equals(payer)) {
            throw new RuntimeException("You are not authorized to update this expense");
        }
        
        expense.setDescription(dto.getDescription());
        expense.setTotalAmount(dto.getTotalAmount());
        expense.getSplits().clear();
        
        List<ExpenseSplit> newSplits = new ArrayList<>();
        if (dto.getSplits() != null && !dto.getSplits().isEmpty()) {
            for (ExpenseSplitDTO splitDTO : dto.getSplits()) {
                ExpenseSplit split = new ExpenseSplit();
                split.setParticipant(splitDTO.getParticipant());
                split.setSplitAmount(splitDTO.getSplitAmount());
                split.setExpense(expense);
                newSplits.add(split);
            }
        } else if (dto.getParticipants() != null && !dto.getParticipants().isEmpty()) {
            int count = dto.getParticipants().size();
            BigDecimal share = dto.getTotalAmount().divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP);
            for (String participant : dto.getParticipants()) {
                ExpenseSplit split = new ExpenseSplit();
                split.setParticipant(participant);
                split.setSplitAmount(share);
                split.setExpense(expense);
                newSplits.add(split);
            }
        }
        expense.getSplits().addAll(newSplits);
        Expense updatedExpense = expenseRepository.save(expense);
        return mapToExpenseResponseDTO(updatedExpense);
    }

    @Override
    public void deleteExpense(Long id, String payer) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!expense.getPayer().equals(payer)) {
            throw new RuntimeException("You are not authorized to delete this expense");
        }
        expenseRepository.delete(expense);
    }

    @Override
    public ExpenseResponseDTO getExpenseById(Long id, String payer) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        return mapToExpenseResponseDTO(expense);
    }

    @Override
    public List<ExpenseResponseDTO> getExpensesForUser(String payer) {
        List<Expense> expenses = expenseRepository.findByPayer(payer);
        return expenses.stream()
                .map(this::mapToExpenseResponseDTO)
                .collect(Collectors.toList());
    }
    
    private ExpenseResponseDTO mapToExpenseResponseDTO(Expense expense) {
        ExpenseResponseDTO dto = new ExpenseResponseDTO();
        dto.setId(expense.getId());
        dto.setDescription(expense.getDescription());
        dto.setTotalAmount(expense.getTotalAmount());
        dto.setPayer(expense.getPayer());
        dto.setCreatedAt(expense.getCreatedAt());
        List<ExpenseSplitDTO> splitDTOs = expense.getSplits().stream().map(split -> {
            ExpenseSplitDTO splitDTO = new ExpenseSplitDTO();
            splitDTO.setParticipant(split.getParticipant());
            splitDTO.setSplitAmount(split.getSplitAmount());
            return splitDTO;
        }).collect(Collectors.toList());
        dto.setSplits(splitDTOs);
        return dto;
    }
}
