package ru.bulatmukhutdinov.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bulatmukhutdinov.persistance.model.CategoryText;
import ru.bulatmukhutdinov.persistance.model.Lang;

import java.util.List;

public interface CategoryTextRepository extends JpaRepository<CategoryText, Long> {
    List<CategoryText> findByLang(Lang lang);

    @Override
    void delete(CategoryText categoryText);
}
