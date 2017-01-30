package ru.bulatmukhutdinov.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({ "ru.bulatmukhutdinov.task" })
public class SpringTaskConfig {

}
