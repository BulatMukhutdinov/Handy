package ru.bulatmukhutdinov.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bulatmukhutdinov.persistance.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);

    @Override
    void delete(Account account);
}
