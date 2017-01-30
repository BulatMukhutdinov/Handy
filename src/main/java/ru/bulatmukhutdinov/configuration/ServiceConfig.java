package ru.bulatmukhutdinov.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "ru.bulatmukhutdinov.service" })
public class ServiceConfig {
}
