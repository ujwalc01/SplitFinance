package com.splitfinance.settlement.service;

import com.splitfinance.auth.entity.User;
import com.splitfinance.expense.entity.Expense;
import com.splitfinance.expense.entity.ExpenseSplit;
import com.splitfinance.expense.repository.ExpenseRepository;
import com.splitfinance.group.entity.UserGroup;
import com.splitfinance.group.repository.UserGroupRepository;
import com.splitfinance.settlement.dto.SettlementTransactionDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Transactional
public class SettlementServiceImpl implements SettlementService {

    private final UserGroupRepository userGroupRepository;
    private final ExpenseRepository expenseRepository;

    @Autowired
    public SettlementServiceImpl(UserGroupRepository userGroupRepository,
                                 ExpenseRepository expenseRepository) {
        this.userGroupRepository = userGroupRepository;
        this.expenseRepository = expenseRepository;
    }

    @Override
    public List<SettlementTransactionDTO> calculateSettlements(Long groupId) {
        // Fetch the group based on the provided group ID.
        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        // Initialize a map to hold net balances for each group member.
        // Positive value: member is owed money.
        // Negative value: member owes money.
        Map<String, BigDecimal> netBalances = new HashMap<>();
        for (User member : group.getMembers()) {
            netBalances.put(member.getUsername(), BigDecimal.ZERO);
        }

        // Retrieve all expenses from the system.
        // For each expense, include it only if:
        //   - The expense's payer is a member of this group.
        //   - All split participants of the expense belong to this group.
        List<Expense> allExpenses = expenseRepository.findAll();
        for (Expense expense : allExpenses) {
            if (!netBalances.containsKey(expense.getPayer())) {
                // Skip expenses where the payer is not in this group.
                continue;
            }
            boolean splitsAllInGroup = true;
            for (ExpenseSplit split : expense.getSplits()) {
                if (!netBalances.containsKey(split.getParticipant())) {
                    splitsAllInGroup = false;
                    break;
                }
            }
            if (!splitsAllInGroup) continue;

            // Update net balance:
            // Payer receives credit equal to the total expense amount.
            netBalances.put(expense.getPayer(),
                    netBalances.get(expense.getPayer()).add(expense.getTotalAmount()));
            // For each split, deduct the amount the participant owes.
            for (ExpenseSplit split : expense.getSplits()) {
                String participant = split.getParticipant();
                netBalances.put(participant,
                        netBalances.get(participant).subtract(split.getSplitAmount()));
            }
        }

        // Settlement algorithm:
        // Create lists of creditors (net balance > 0) and debtors (net balance < 0).
        // Use a greedy algorithm to match the largest creditor with the largest debtor.
        PriorityQueue<Map.Entry<String, BigDecimal>> creditors = new PriorityQueue<>(
                Comparator.comparing(Map.Entry<String, BigDecimal>::getValue, Comparator.reverseOrder())
        );
        PriorityQueue<Map.Entry<String, BigDecimal>> debtors = new PriorityQueue<>(
                Comparator.comparing(Map.Entry<String, BigDecimal>::getValue)
        );
        for (Map.Entry<String, BigDecimal> entry : netBalances.entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(entry);
            } else if (entry.getValue().compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(entry);
            }
        }

        List<SettlementTransactionDTO> transactions = new ArrayList<>();

        // Match creditors and debtors until either queue is empty.
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            Map.Entry<String, BigDecimal> creditor = creditors.poll();
            Map.Entry<String, BigDecimal> debtor = debtors.poll();

            BigDecimal creditAmount = creditor.getValue();
            BigDecimal debtAmount = debtor.getValue().abs(); // convert debt to positive for comparison

            // Determine the settlement amount as the minimum of the two amounts.
            BigDecimal settlementAmount = creditAmount.min(debtAmount).setScale(2, RoundingMode.HALF_UP);

            // Record the transaction: debtor pays creditor.
            SettlementTransactionDTO transaction = new SettlementTransactionDTO();
            transaction.setFrom(debtor.getKey());
            transaction.setTo(creditor.getKey());
            transaction.setAmount(settlementAmount);
            transactions.add(transaction);

            // Adjust the credit and debt.
            BigDecimal newCredit = creditAmount.subtract(settlementAmount);
            BigDecimal newDebt = debtor.getValue().add(settlementAmount); // note: debtor's value is negative

            // Return the updated balances to their queues if non-zero.
            if (newCredit.compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(new AbstractMap.SimpleEntry<>(creditor.getKey(), newCredit));
            }
            if (newDebt.compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(new AbstractMap.SimpleEntry<>(debtor.getKey(), newDebt));
            }
        }
        return transactions;
    }
}
