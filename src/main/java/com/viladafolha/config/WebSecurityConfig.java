package com.viladafolha.config;

import com.viladafolha.controllers.service.InhabitantService;
import com.viladafolha.filters.CustomAuthenticationProvider;
import com.viladafolha.filters.JWTAuthenticationFilter;
import com.viladafolha.filters.JWTAuthorizationFilter;
import com.viladafolha.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final JWTUtil jwtUtil;
    private final CustomAuthenticationProvider authenticationProvider;
    private InhabitantService inhabitantService;


    public WebSecurityConfig(InhabitantService inhabitantService, JWTUtil jwtUtil, CustomAuthenticationProvider authenticationProvider) {
        this.inhabitantService = inhabitantService;
        this.jwtUtil = jwtUtil;
        this.authenticationProvider = authenticationProvider;
    }

    private static final String[] PUBLIC_MATCHERS_POST = {"/login/**"};

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inhabitantService).passwordEncoder(getPasswordEncoder());
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/village/inhabitants/").hasAnyRole("USER", "ADMIN")
                .antMatchers("/village/inhabitants/create", "/village/financial/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(
                        new JWTAuthenticationFilter(authenticationManager(), jwtUtil)
                ).addFilterAfter(
                        new JWTAuthorizationFilter(authenticationManager(), jwtUtil, inhabitantService),
                        JWTAuthenticationFilter.class
                );

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Bean(name = "passEncoder")
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
