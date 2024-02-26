package com.api.AFTAS.security;

import com.api.AFTAS.security.User.Role;
import com.api.AFTAS.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/auth/**"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    //private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n\n\n");
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers(HttpMethod.GET,"/competition").hasAnyAuthority("Jury", "Manager", "Adherent")
                                .requestMatchers(HttpMethod.POST,"/competition").hasAnyAuthority("Jury", "Manager")
                                .requestMatchers(HttpMethod.DELETE,"/competition").hasAnyAuthority("Jury", "Manager")
                                .requestMatchers(HttpMethod.PUT,"/competition").hasAnyAuthority("Jury", "Manager")

                                .requestMatchers("/fish").hasAnyAuthority("Jury", "Manager")
                                .requestMatchers("/hunting").hasAnyAuthority("Jury", "Manager")
                                .requestMatchers("/level").hasAnyAuthority("Manager")
                                .requestMatchers("/member").hasAnyAuthority("Manager")
                                .requestMatchers("/ranking/rapport").hasAnyAuthority("Jury", "Manager", "Adherent")
                                .requestMatchers("/ranking").hasAnyAuthority("Jury", "Manager")
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }


}
