package ru.bulatmukhutdinov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.persistance.dao.AccountRepository;
import ru.bulatmukhutdinov.persistance.dao.RoleRepository;

import java.util.HashSet;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(Account account) {
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        account.setRoles(new HashSet<>(roleRepository.findAll()));
        accountRepository.save(account);
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
