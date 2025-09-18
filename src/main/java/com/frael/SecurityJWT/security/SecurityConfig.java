package com.frael.SecurityJWT.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.frael.SecurityJWT.security.filters.JwtAuthenticationFilter;
import com.frael.SecurityJWT.security.filters.JwtAuthorizationFilter;
import com.frael.SecurityJWT.security.jwt.JwtUtils;
import com.frael.SecurityJWT.services.UserDetailsServiceImpl;

//import org.springframework.security.config.Customizer;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMethodSecurity
public class SecurityConfig {

    // Filtro de autenticación
    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    JwtUtils jwtUtils;

    // Injectamos nuestro servicio personalizado de UserDetailsService
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager)
            throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        // Seteamos el manager de autenticacion
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);

        return httpSecurity.csrf(c -> c.disable())
                .authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers("/dashboard/modulo1").permitAll();

                            // Para manejar el acceso por ROLES - 1era Forma
                            /*
                             * auth.requestMatchers("/api/auth/createUser").hasRole("ADMIN");
                             * auth.requestMatchers("/api/role/accessAdmin").hasRole("ADMIN");
                             * auth.requestMatchers("/api/role/accessUser").hasRole("USER");
                             * auth.requestMatchers("/dashboard/menu2").hasAnyRole("USER", "ADMIN");
                             */
                            auth.anyRequest().authenticated();
                        })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                // .httpBasic(Customizer.withDefaults())
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class) // Agregamos el
                                                                                                     // filtro de
                                                                                                     // autorización -
                                                                                                     // PONER EL FILTRO
                                                                                                     // CORRECTO -.-
                .build();
    }

    /**
     * Creacion de un usuario en memoria para una autenticacion básica
     * 
     * @apiNote Antes con la contraseña en texto plano daba error porque se habia
     *          cambiado el metodo de encriptamiento al metodo
     *          passwordEncoder(),
     * 
     * @return
     */
    /*
     * @Bean
     * UserDetailsService userDetailService(){
     * InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
     * manager.createUser(User.withUsername("frael").password(passwordEncoder().
     * encode("1234")).roles().build());
     * 
     * return manager;
     * }
     */

    /**
     * Creacion del objeto encargado de la administracion de la autenticacion del
     * usuario
     * 
     * @param httpSecurity
     * @param passwordEncoder
     * @return
     */
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder)
            throws Exception {

        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                // .userDetailsService(userDetailService())
                .userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder).and().build();
    }

    /**
     * Sobreescribe el tipo de metodo de encriptacion para las contraseñas
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
