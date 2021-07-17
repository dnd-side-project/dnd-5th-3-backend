package com.dnd5th3.dnd5th3backend.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/**").permitAll()
                    .antMatchers("/").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilterBefore(customLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CustomLoginProcessingFilter customLoginProcessingFilter() throws Exception {
        CustomLoginProcessingFilter customLoginProcessingFilter =  new CustomLoginProcessingFilter();
        customLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        customLoginProcessingFilter.setAuthenticationSuccessHandler(successHandler);
        customLoginProcessingFilter.setAuthenticationFailureHandler(failureHandler);
        return customLoginProcessingFilter;
    }
}
