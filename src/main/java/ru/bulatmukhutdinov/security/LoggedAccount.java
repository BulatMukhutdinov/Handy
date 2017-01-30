package ru.bulatmukhutdinov.security;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.List;

@Component
public class LoggedAccount implements HttpSessionBindingListener {

    private String username;
    private ActiveAccountStore activeAccountStore;

    public LoggedAccount(String username, ActiveAccountStore activeAccountStore) {
        this.username = username;
        this.activeAccountStore = activeAccountStore;
    }

    public LoggedAccount() {
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        System.out.println(activeAccountStore);
        List<String> accounts = activeAccountStore.getAccounts();
        LoggedAccount account = (LoggedAccount) event.getValue();
        if (!accounts.contains(account.getUsername())) {
            accounts.add(account.getUsername());
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        List<String> accounts = activeAccountStore.getAccounts();
        LoggedAccount account = (LoggedAccount) event.getValue();
        if (accounts.contains(account.getUsername())) {
            accounts.remove(account.getUsername());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
