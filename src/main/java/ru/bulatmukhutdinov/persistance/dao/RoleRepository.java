package ru.bulatmukhutdinov.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bulatmukhutdinov.persistance.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    @Override
    void delete(Role role);
}
