package ru.bulatmukhutdinov.service;


import ru.bulatmukhutdinov.persistance.model.CategoryText;
import ru.bulatmukhutdinov.persistance.model.Lang;

import java.util.List;

public interface CategoryTextService {

    List<CategoryText> findCategoryTextByLang(Lang lang);
}
