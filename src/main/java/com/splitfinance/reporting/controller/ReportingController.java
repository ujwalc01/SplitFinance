package com.splitfinance.reporting.controller;

import com.splitfinance.reporting.dto.ExpenseReportDTO;
import com.splitfinance.reporting.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportingController {

    private final ReportingService reportingService;

    @Autowired
    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }
    
    // Generate an expense report for a given group.
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<ExpenseReportDTO> getExpenseReportForGroup(@PathVariable Long groupId) {
         ExpenseReportDTO report = reportingService.generateExpenseReportForGroup(groupId);
         return ResponseEntity.ok(report);
    }
}
