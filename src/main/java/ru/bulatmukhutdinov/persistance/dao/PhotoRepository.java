package ru.bulatmukhutdinov.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bulatmukhutdinov.persistance.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
