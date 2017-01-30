package ru.bulatmukhutdinov.service;

import ru.bulatmukhutdinov.dto.AccountDto;
import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.persistance.model.PasswordResetToken;
import ru.bulatmukhutdinov.persistance.model.VerificationToken;
import ru.bulatmukhutdinov.web.error.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface AccountService {

    Account registerNewAccount(AccountDto accountDto) throws AccountAlreadyExistException;

    Account getAccount(String verificationToken);

    void saveRegisteredAccount(Account account);

    void deleteAccount(Account account);

    void createVerificationTokenForAccount(Account account, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForAccount(Account account, String token);

    Account findAccountByEmail(String email);

    PasswordResetToken getPasswordResetToken(String token);

    Account getAccountByPasswordResetToken(String token);

    Account getAccountByID(long id);

    void changeAccountPassword(Account account, String password);

    boolean checkIfValidOldPassword(Account account, String password);

    String validateVerificationToken(String token);

    String generateQRUrl(Account Account) throws UnsupportedEncodingException;

    Account updateAccount2FA(boolean use2FA);

    List<String> getAccountsFromSessionRegistry();

}
