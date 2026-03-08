


package com.safeshield.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())  // disable CSRF
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()   // allow all requests
            );

        return http.build();
    }
}
/*package com.safeshield.app.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ================================
                // 1️⃣ CSRF
                // ================================
                // Disable for development; enable in production
                .csrf(csrf -> csrf.disable())

                // ================================
                // 2️⃣ Authorize Requests
                // ================================
                .authorizeHttpRequests(auth -> auth
                        // Public pages
                        .requestMatchers(
                                "/",
                                "/victim/**",
                                "/counselor/**",
                                "/police/**",
                                "/admin/**",
                                "/css/**",
                                "/js/**",
                                "/h2-console/**"
                        ).permitAll()
                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )

                // ================================
                // 3️⃣ Form Login Configuration
                // ================================
                // We will use manual login in controller, so disable Spring default login
                .formLogin(form -> form.disable())

                // ================================
                // 4️⃣ HTTP Basic (optional, for testing APIs)
                // ================================
                .httpBasic(httpBasic -> httpBasic.disable())

                // ================================
                // 5️⃣ Allow H2 Console frames
                // ================================
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    // ================================
    // 6️⃣ Password Encoder
    // ================================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}*/