package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.FinancialSummary;
import com.devyforu.pibanks.Repository.FinancialSummaryDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@AllArgsConstructor
public class FinancialSummaryService {
    private FinancialSummaryDAO summary;

    public FinancialSummary getFinancialSummaryBetweenDates(Date startDate, Date endDate) {
        return summary.getFinancialSummaryBetweenDates(startDate, endDate);
    }
}
