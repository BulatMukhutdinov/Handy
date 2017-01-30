package ru.bulatmukhutdinov.registration;

import org.springframework.context.ApplicationEvent;
import ru.bulatmukhutdinov.persistance.model.Account;

import java.util.Locale;

@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final String appUrl;
    private final Locale locale;
    private final Account account;

    public OnRegistrationCompleteEvent(final Account account, final Locale locale, final String appUrl) {
        super(account);
        this.account = account;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    //

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public Account getAccount() {
        return account;
    }

}
