package ru.bulatmukhutdinov.dto;

/**
 * Created by Reverendo on 16.03.2017.
 */
public class CategoryDto {
    private String name;

    public CategoryDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
