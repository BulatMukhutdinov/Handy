package ru.bulatmukhutdinov.security;

import java.util.ArrayList;
import java.util.List;

public class ActiveAccountStore {

    public List<String> accounts;

    public ActiveAccountStore() {
        accounts = new ArrayList<String>();
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }
}
