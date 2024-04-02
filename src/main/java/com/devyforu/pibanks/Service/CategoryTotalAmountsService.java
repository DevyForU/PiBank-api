package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.CategoryTotalAmounts;
import com.devyforu.pibanks.Repository.CategoryTotalAmountsDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@AllArgsConstructor
public class CategoryTotalAmountsService {
    private CategoryTotalAmountsDAO totalAmounts;

    public CategoryTotalAmounts getAmountCategoryBetweenDates(Date startDate, Date endDate) {
        return totalAmounts.getAmountCategoryBetweenDates(startDate, endDate);
    }
}
