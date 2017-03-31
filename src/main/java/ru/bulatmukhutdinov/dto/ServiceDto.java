package ru.bulatmukhutdinov.dto;

/**
 * Created by Reverendo on 31.03.2017.
 */
public class ServiceDto {

    private String description;

    private String category;

    public ServiceDto() {
    }

    public ServiceDto(String description, String category) {
        this.description = description;
        this.category = category;
    }

    public ServiceDto(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
