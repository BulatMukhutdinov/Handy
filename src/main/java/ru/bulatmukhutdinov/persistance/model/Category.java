package ru.bulatmukhutdinov.persistance.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToMany(mappedBy = "categories")
    private Set<Account> accounts;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<CategoryText> categoryTexts;

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Set<CategoryText> getCategoryTexts() {
        return categoryTexts;
    }

    public void setCategoryTexts(Set<CategoryText> categoryTexts) {
        this.categoryTexts = categoryTexts;
    }
}
