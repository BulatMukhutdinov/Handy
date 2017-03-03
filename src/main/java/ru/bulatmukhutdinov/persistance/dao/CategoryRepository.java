package ru.bulatmukhutdinov.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bulatmukhutdinov.persistance.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Override
    void delete(Category category);
}
