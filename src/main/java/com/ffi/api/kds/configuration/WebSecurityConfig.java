package com.ffi.api.kds.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class WebSecurityConfig {
    @Value("${app.auth.basic.username}")
    private String kdsUsername;

    @Value("${app.auth.basic.password}")
    private String kdsPassword;

    @Value("${app.outletCode}")
    private String outletCode;

    @Value("${app.external.properties}")
    private String externalProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(crsf -> crsf.disable())
                .cors(cors -> corsConfigurationSource())
                .authorizeHttpRequests(u -> u.antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**")
                        .permitAll().anyRequest().authenticated())
                .httpBasic(basic -> Customizer.withDefaults())
                .formLogin(form -> form.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        System.out.println("============================================================================");
        System.out
                .println("===============              outlet code : " + outletCode + "            =================");
        System.out.println("============================================================================");
        System.out.println("Attention!!! Please ensure kds.properties file is available in " + externalProperties);
        System.out.println("============================================================================");
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username(kdsUsername)
                .password(kdsPassword)
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
