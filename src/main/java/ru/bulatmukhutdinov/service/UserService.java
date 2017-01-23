package ru.bulatmukhutdinov.service;


import ru.bulatmukhutdinov.persistance.model.Account;

public interface UserService {
    void save(Account user);

    Account findByUsername(String username);
}
