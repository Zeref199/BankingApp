package com.bank.front.security;

import com.bank.front.security.oauth.CustomerOAuth2UserService;
import com.bank.front.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Autowired
    private CustomerOAuth2UserService oAuth2UserService;
    @Autowired
    private OAuth2LoginSuccessHandler oauth2LoginHandler;
    @Autowired
    private DataBaseLoginSuccessHandler databaseLoginHandler;

    @Bean
    public UserDetailsService UserDetailsService(){
        return new CustomerUserDetailsService();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(UserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider())
                  .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                          .requestMatchers("/HomePage", "/account_details", "/update_account_details").authenticated()
                          .anyRequest().permitAll())
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(databaseLoginHandler)
                        .defaultSuccessUrl("/app/HomePage")
                        .permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // Set the login page
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService) // Inject your OAuth2UserService
                        )
                        .successHandler(oauth2LoginHandler)
                        .defaultSuccessUrl("/app/HomePage")
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .rememberMe((remember) -> remember
                        .key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
                        .tokenValiditySeconds(14 * 24 * 60 * 60))
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }

}
