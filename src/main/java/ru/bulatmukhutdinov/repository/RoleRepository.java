package ru.bulatmukhutdinov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bulatmukhutdinov.persistance.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
