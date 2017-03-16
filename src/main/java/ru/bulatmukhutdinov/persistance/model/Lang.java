package ru.bulatmukhutdinov.persistance.model;

import javax.persistence.*;;import java.util.Set;

@Entity
@Table(name = "lang")
public class Lang {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "lang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CategoryText> categoryTexts;


    public Set<CategoryText> getCategoryTexts() {
        return categoryTexts;
    }

    public void setCategoryTexts(Set<CategoryText> categoryTexts) {
        this.categoryTexts = categoryTexts;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lang lang = (Lang) o;

        if (!id.equals(lang.id)) return false;
        return name.equals(lang.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Lang{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
