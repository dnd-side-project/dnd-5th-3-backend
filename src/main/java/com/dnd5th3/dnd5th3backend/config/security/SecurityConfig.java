package com.dnd5th3.dnd5th3backend.config.security;

import com.dnd5th3.dnd5th3backend.config.security.handler.CustomAccessDeniedHandler;
import com.dnd5th3.dnd5th3backend.config.security.handler.CustomAuthenticationFailureHandler;
import com.dnd5th3.dnd5th3backend.config.security.handler.CustomAuthenticationSuccessHandler;
import com.dnd5th3.dnd5th3backend.config.security.jwt.JwtAuthenticationFilter;
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
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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
                        .antMatchers("/api/v1/member/test").hasRole("USER")
                        .anyRequest().authenticated()
                    .and()
                        .addFilterBefore(customLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

            http
                    .exceptionHandling()
                    .authenticationEntryPoint(new CustomLoginAuthenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler);

            http
                    .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);

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
