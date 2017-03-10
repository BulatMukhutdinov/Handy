package ru.bulatmukhutdinov.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bulatmukhutdinov.persistance.model.Lang;

public interface LangRepository extends JpaRepository<Lang, Long> {

}
