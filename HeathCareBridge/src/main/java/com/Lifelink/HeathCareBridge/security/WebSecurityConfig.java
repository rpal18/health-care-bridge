package com.Lifelink.HeathCareBridge.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private final  UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPoint authEntryPoint;
    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService ,
                             AuthEntryPoint authEntryPoint
    ){
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }
    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable) .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                authorizeHttpRequests(
                        req -> req.requestMatchers("/api/public/admin/my-facility").hasAuthority("ORG_ADMIN").
                                requestMatchers("/api/facilities/admin/**").hasAuthority("SYSTEM_ADMIN").
                                requestMatchers("/api/resources/**").hasAuthority("ORG_ADMIN").
                                requestMatchers("/api/public/admin/register").permitAll().
                                requestMatchers("/api/facilities/request").permitAll().
                                requestMatchers("/api/auth/**").permitAll().
                                requestMatchers("/v3/api-doc/**").permitAll().
                                requestMatchers("/swagger-ui/**").permitAll().
                                requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/error").permitAll().anyRequest().authenticated()


                );
        http.addFilterBefore(authTokenFilter() , UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().requestMatchers("/v3/api-docs" ,
                "/swagger-resources/**" , "/configuration-ui" , "/configuration/security" ,
                "/swagger-ui.html" , "/webjars/**"
        ));
    }
}
