package ru.bulatmukhutdinov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bulatmukhutdinov.dto.AccountDto;
import ru.bulatmukhutdinov.persistance.model.Role;
import ru.bulatmukhutdinov.web.error.AccountAlreadyExistException;
import ru.bulatmukhutdinov.persistance.dao.AccountRepository;
import ru.bulatmukhutdinov.persistance.dao.PasswordResetTokenRepository;
import ru.bulatmukhutdinov.persistance.dao.RoleRepository;
import ru.bulatmukhutdinov.persistance.dao.VerificationTokenRepository;
import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.persistance.model.PasswordResetToken;
import ru.bulatmukhutdinov.persistance.model.VerificationToken;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRegistry sessionRegistry;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "SpringRegistration";

    // API

    @Override
    public Account registerNewAccount(final AccountDto accountDto) {
        if (emailExist(accountDto.getEmail())) {
            throw new AccountAlreadyExistException("There is an account with that email adress: " + accountDto.getEmail());
        }
        final Account account = new Account();

        account.setFirstName(accountDto.getFirstName());
        account.setLastName(accountDto.getLastName());
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        account.setEmail(accountDto.getEmail());
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        account.setRoles(roles);
        return repository.save(account);
    }

    @Override
    public Account getAccount(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getAccount();
        }
        return null;
    }

    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredAccount(final Account Account) {
        repository.save(Account);
    }

    @Override
    public void deleteAccount(final Account account) {
        final VerificationToken verificationToken = tokenRepository.findByAccount(account);

        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }

        final PasswordResetToken passwordToken = passwordTokenRepository.findByAccount(account);

        if (passwordToken != null) {
            passwordTokenRepository.delete(passwordToken);
        }

        repository.delete(account);
    }

    @Override
    public void createVerificationTokenForAccount(final Account account, final String token) {
        final VerificationToken myToken = new VerificationToken(token, account);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID().toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public void createPasswordResetTokenForAccount(final Account account, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, account);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public Account findAccountByEmail(final String email) {
        return repository.findByEmail(email);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public Account getAccountByPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token).getAccount();
    }

    @Override
    public Account getAccountByID(final long id) {
        return repository.findOne(id);
    }

    @Override
    public void changeAccountPassword(final Account account, final String password) {
        account.setPassword(passwordEncoder.encode(password));
        repository.save(account);
    }

    @Override
    public boolean checkIfValidOldPassword(final Account account, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, account.getPassword());
    }

    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final Account account = verificationToken.getAccount();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        account.setEnabled(true);
        // tokenRepository.delete(verificationToken);
        repository.save(account);
        return TOKEN_VALID;
    }

    @Override
    public String generateQRUrl(Account Account) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, Account.getEmail(),  APP_NAME), "UTF-8");
    }

    @Override
    public Account updateAccount2FA(boolean use2FA) {
        final Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();
        Account principal = (Account) curAuth.getPrincipal();
//        principal.setUsing2FA(use2FA);
        principal = repository.save(principal);
        final Authentication auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), curAuth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return principal;
    }

    private boolean emailExist(final String email) {
        return repository.findByEmail(email) != null;
    }

    @Override
    public List<String> getAccountsFromSessionRegistry() {
        return sessionRegistry.getAllPrincipals().stream().filter((u) -> !sessionRegistry.getAllSessions(u, false).isEmpty()).map(Object::toString).collect(Collectors.toList());
    }

}