package ru.bulatmukhutdinov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.repository.AccountRepository;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(Account user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        accountRepository.save(user);
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
