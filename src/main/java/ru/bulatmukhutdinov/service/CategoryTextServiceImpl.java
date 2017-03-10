package ru.bulatmukhutdinov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bulatmukhutdinov.persistance.dao.CategoryTextRepository;
import ru.bulatmukhutdinov.persistance.model.CategoryText;
import ru.bulatmukhutdinov.persistance.model.Lang;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryTextServiceImpl implements CategoryTextService {

    @Autowired
    private CategoryTextRepository categoryTextRepository;

    @Override
    public List<CategoryText> findCategoryTextByLang(Lang lang) {
        return categoryTextRepository.findByLang(lang);
    }
}