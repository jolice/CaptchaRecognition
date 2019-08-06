package me.nextgeneric.captcha.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    public SecurityConfigurer(TokenAuthenticationFilter tokenAuthenticationFilter) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
            http
                    .addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class)
                    .authorizeRequests()
                       .antMatchers("/account/**").permitAll()
                       .anyRequest().authenticated()
                       .and()
                    .exceptionHandling()
                       .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                       .and()
                    .httpBasic().authenticationEntryPoint(authenticationEntryPoint())
                       .and()
                    .formLogin()
                       .disable()
                    .csrf()
                       .disable();
    }

    @Bean
    public BasicAuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationFailureEntryPoint();
    }


}
