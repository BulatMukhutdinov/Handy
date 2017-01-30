package ru.bulatmukhutdinov.security;

public interface SecurityAccountService {

    String validatePasswordResetToken(long id, String token);

}
