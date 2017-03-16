package ru.bulatmukhutdinov.service;


import ru.bulatmukhutdinov.persistance.model.Category;

import java.util.List;

public interface CategoryService {


    List<Category> findAll();
}
