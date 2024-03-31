package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.Category;
import com.devyforu.pibanks.Repository.CategoryDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryDAO category;

    public List<Category> findAll(){
       return category.findAll();
    }
    public Category save(Category toSave) {
        return category.save(toSave);
    }

    public void deleteById(String accountNumber) {
        category.deleteById(accountNumber);
    }

    public Category getById(String accountNumber) {
        return category.getById(accountNumber);
    }
}
