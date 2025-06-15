package com.splitfinance.settlement.controller;

import com.splitfinance.settlement.dto.SettlementTransactionDTO;
import com.splitfinance.settlement.service.SettlementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    @Autowired
    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    // Endpoint to calculate settlements for a group by its groupId.
    @GetMapping("/{groupId}")
    public ResponseEntity<List<SettlementTransactionDTO>> getSettlements(@PathVariable Long groupId) {
        List<SettlementTransactionDTO> settlements = settlementService.calculateSettlements(groupId);
        return ResponseEntity.ok(settlements);
    }
}
