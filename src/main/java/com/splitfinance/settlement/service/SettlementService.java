package com.splitfinance.settlement.service;

import java.util.List;

import com.splitfinance.settlement.dto.SettlementTransactionDTO;

public interface SettlementService {
    // Calculate settlements for a given group ID.
    List<SettlementTransactionDTO> calculateSettlements(Long groupId);
}
