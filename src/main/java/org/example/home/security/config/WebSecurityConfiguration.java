package org.example.home.security.config;

import lombok.AllArgsConstructor;
import org.example.home.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsService userDetailsService;
    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring().antMatchers("/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/registered").not().fullyAuthenticated()
                .and()
                .authorizeRequests().antMatchers( "/book").hasAnyRole("USER","ADMIN")
                .and()
                .authorizeRequests().antMatchers("/book/add/**", "/book/edit/*", "/book/edit/delete/*").hasRole("ADMIN")
                .and()

                .formLogin()
                .loginProcessingUrl("/custom_spring_security_check")
                .usernameParameter("custom_username").passwordParameter("custom_password").loginPage("/login")
                .defaultSuccessUrl("/book").failureUrl("/error")
                .and()

                .logout()
                .permitAll()
                .logoutSuccessUrl("/");;

    }

//    @SuppressWarnings("deprecation")
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }
//    @SuppressWarnings("deprecation")
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(daoAuthenticationProvider());
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }



//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider =
//                new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(bCryptPasswordEncoder);
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }


}
