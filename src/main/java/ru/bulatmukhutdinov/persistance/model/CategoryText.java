package ru.bulatmukhutdinov.persistance.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class CategoryText {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "lang_id")
    private Lang lang;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
