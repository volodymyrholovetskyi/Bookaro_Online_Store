package com.volodymyr.bookaro.security;

import com.volodymyr.bookaro.user.db.UserEntityRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@AllArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties(AdminConfig.class)
@Profile("!test")
public class BookaroSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserEntityRepository userEntityRepository;
    private final AdminConfig config;

    @Bean
    User systemUser() {
        return config.adminUser();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/catalog/**", "/uploads/**", "/authors/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/orders", "/login", "/users").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);


    }

    @SneakyThrows
    private JsonUsernameAuthenticationFilter authenticationFilter() {
        JsonUsernameAuthenticationFilter filter = new JsonUsernameAuthenticationFilter();
        filter.setAuthenticationManager(super.authenticationManager());
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        BookaroUserDetailsService detailsService = new BookaroUserDetailsService(userEntityRepository, config);
        provider.setUserDetailsService(detailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

/**
 * IN MEMORY
 */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("marek@example.org")
//                .password("{noop}xxx")
//                .roles("USER")
//                .and()
//                .withUser("admin")
//                .password("{noop}xxx")
//                .roles("ADMIN");
//    }
}
