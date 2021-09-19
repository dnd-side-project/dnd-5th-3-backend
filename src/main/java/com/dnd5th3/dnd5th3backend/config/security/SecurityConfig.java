package com.dnd5th3.dnd5th3backend.config.security;

import com.dnd5th3.dnd5th3backend.config.security.handler.CustomAccessDeniedHandler;
import com.dnd5th3.dnd5th3backend.config.security.handler.CustomAuthenticationFailureHandler;
import com.dnd5th3.dnd5th3backend.config.security.handler.CustomAuthenticationSuccessHandler;
import com.dnd5th3.dnd5th3backend.config.security.jwt.JwtAuthenticationFilter;
import com.dnd5th3.dnd5th3backend.config.security.oauth2.CustomOAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationfailureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomOAuth2UserServiceImpl customOAuth2UserService;
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
                        .antMatchers("/api/v1/login").permitAll()
                        .antMatchers("/api/v1/member/reset").permitAll()
                        .antMatchers("/api/v1/member/token").permitAll()
                        .antMatchers("/api/v1/member/exists/**").permitAll()
                        .antMatchers(HttpMethod.POST,"/api/v1/member").permitAll()
                        .antMatchers("/api/v1/posts/**").permitAll()
                        .antMatchers("/api/v1/mypage").permitAll()
                        .antMatchers("/docs/**").permitAll()
                        .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                        .addFilterBefore(customAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                        .oauth2Login()
                        .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                        .and()
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationfailureHandler);
            http
                    .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler);

            http
                    .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public CustomAuthenticationProcessingFilter customAuthenticationProcessingFilter() throws Exception {
        CustomAuthenticationProcessingFilter customAuthenticationProcessingFilter =  new CustomAuthenticationProcessingFilter();
        customAuthenticationProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        customAuthenticationProcessingFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        customAuthenticationProcessingFilter.setAuthenticationFailureHandler(customAuthenticationfailureHandler);
        return customAuthenticationProcessingFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
