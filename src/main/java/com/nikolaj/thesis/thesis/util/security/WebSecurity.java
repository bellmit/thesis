package com.nikolaj.thesis.thesis.util.security;

import com.nikolaj.thesis.thesis.persistence.DPUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.nikolaj.thesis.thesis.util.security.SecurityConstants.SIGN_UP_URL;
import static com.nikolaj.thesis.thesis.util.security.SecurityConstants.URL;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private DPUserRepository DPUserRepository;



    public WebSecurity(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, DPUserRepository DPUserRepository) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.DPUserRepository = DPUserRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL, URL).permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/dpuser/signup").permitAll()
                .antMatchers("/data/addFile").permitAll()
                .antMatchers("/data/addDataPoints").permitAll()
                .antMatchers("/questionaire/getall").permitAll()
                .antMatchers("/experimentdefinitions/add").permitAll()
                .antMatchers("/experiments/add").permitAll()
                .antMatchers("/datadefinitions/add").permitAll()
                .antMatchers("/datasets/add").permitAll()
                .antMatchers("/project","/phase","/device","/mqtt").hasRole(
                        "projectmember")
                .antMatchers("/device","/data").hasRole("datasubject")
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthFilter(authenticationManager(), DPUserRepository))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Accept", "X-Requested-With","Origin", "remember-me","authorization"));
        configuration.setAllowedMethods(Arrays.asList("GET","OPTIONS","POST","PUT", "DELETE"));
        configuration.setExposedHeaders(Arrays.asList("authorization", "id","X-Auth-Token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
