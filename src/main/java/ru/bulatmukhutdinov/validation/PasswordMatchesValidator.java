package ru.bulatmukhutdinov.validation;


import ru.bulatmukhutdinov.dto.AccountDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final AccountDto accountDto = (AccountDto) obj;
        return accountDto.getPassword().equals(accountDto.getMatchingPassword());
    }

}
