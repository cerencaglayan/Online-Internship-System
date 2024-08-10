package com.g7.config;

import com.g7.model.Company;
import com.g7.model.IztechUser;
import com.g7.repository.CompanyRepository;
import com.g7.service.UbysApi;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@AllArgsConstructor
public class AppConfig {

    private final CompanyRepository companyRepository;
    private final UbysApi ubysApi;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            if (username == null) {
                throw new UsernameNotFoundException("User Not Found");
            }

            // TODO: This is more secure, enable this on production
//            if (username.contains("@std.iyte.edu.tr") || username.contains("@iyte.edu.tr")) {
//                IztechUser maybeIztechUser = ubysApi.getStudent(username);
//                if (maybeIztechUser == null) {
//                    throw new UsernameNotFoundException("User Not Found");
//                }
//                return maybeIztechUser;
//            }

            IztechUser maybeIztechUser = ubysApi.getStudent(username);
            if (maybeIztechUser != null) {
                return maybeIztechUser;
            }

            Optional<Company> maybeCompany = companyRepository.findByEmail(username);

            return maybeCompany.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
