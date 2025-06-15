package com.splitfinance.reporting.service;

import com.splitfinance.reporting.dto.ExpenseReportDTO;

public interface ReportingService {
    ExpenseReportDTO generateExpenseReportForGroup(Long groupId);
}
