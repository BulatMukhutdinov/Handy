package ru.bulatmukhutdinov.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import ru.bulatmukhutdinov.validation.EmailValidator;
import ru.bulatmukhutdinov.validation.PasswordMatchesValidator;

import java.util.Locale;

@Configuration
@ComponentScan(basePackages = {"ru.bulatmukhutdinov.web"})
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter {

    public MvcConfig() {
        super();
    }

    //

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        super.addViewControllers(registry);
        registry.addViewController("/").setViewName("forward:/login");
        registry.addViewController("/login");
        registry.addViewController("/registration");
        registry.addViewController("/registrationCaptcha");
        registry.addViewController("/logout");
        registry.addViewController("/expiredAccount");
        registry.addViewController("/badUser");
        registry.addViewController("/emailError");
        registry.addViewController("/home");
        registry.addViewController("/invalidSession");
        registry.addViewController("/console");
        registry.addViewController("/admin");
        registry.addViewController("/successRegister");
        registry.addViewController("/forgetPassword");
        registry.addViewController("/updatePassword");
        registry.addViewController("/changePassword");
        registry.addViewController("/accounts");
        registry.addViewController("/qrcode");
    }

    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/", "/resources/");
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }

    // beans

    @Bean
    public LocaleResolver localeResolver() {
        final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
//        cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return cookieLocaleResolver;
    }

//    @Bean
//    public MessageSource messageSource() {
//        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setBasename("i18n/messages");
//        messageSource.setUseCodeAsDefaultMessage(true);
//        messageSource.setDefaultEncoding("UTF-8");
//        messageSource.setCacheSeconds(0);
//        return messageSource;
//    }

    @Bean
    public EmailValidator usernameValidator() {
        return new EmailValidator();
    }

    @Bean
    public PasswordMatchesValidator passwordMatchesValidator() {
        return new PasswordMatchesValidator();
    }

    @Bean
    @ConditionalOnMissingBean(RequestContextListener.class)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

}