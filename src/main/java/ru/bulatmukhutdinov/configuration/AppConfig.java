package ru.bulatmukhutdinov.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bulatmukhutdinov.security.ActiveAccountStore;

@Configuration
public class AppConfig {
    // beans

    @Bean
    public ActiveAccountStore activeUserStore() {
        return new ActiveAccountStore();
    }

}