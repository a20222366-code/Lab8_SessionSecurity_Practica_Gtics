package org.example.lab8_gtics_practica_session_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        String sqlUsuario = "SELECT username, password, enabled FROM users WHERE username = ?";
        String sqlRol = "SELECT u.username, r.name FROM users u "
                + "INNER JOIN roles r ON u.role_id = r.id "
                + "WHERE u.username = ? AND u.enabled = 1";

        users.setUsersByUsernameQuery(sqlUsuario);
        users.setAuthoritiesByUsernameQuery(sqlRol);
        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1. Configuración del formulario de Login personalizado
        http.formLogin(form -> form
                .loginPage("/loginForm")             // Ruta para mostrar mi HTML bonito de login
                .loginProcessingUrl("/processLogin") // Buzón invisible donde Spring procesa y valida los datos
                .defaultSuccessUrl("/dashboard", true) // Si el login es correcto, redirige siempre al dashboard
                .permitAll());                       // Permite que cualquiera pueda ver la pantalla de login

        // 2. Control de accesos y permisos de la aplicación (Reglas de tránsito)
        http.authorizeHttpRequests(auth -> auth
                // Rutas públicas: login, registro y carpetas de diseño (CSS/JS) para que no se rompa la interfaz
                .requestMatchers("/loginForm", "/register", "/css/**", "/js/**").permitAll()
                // Candado general: cualquier otra ruta de la app requiere iniciar sesión obligatoriamente
                .anyRequest().authenticated());


        return http.build();
    }

}


//DB: CREATE DATABASE minispotify_db;
//USE minispotify_db;
//
//CREATE TABLE roles (
//id BIGINT PRIMARY KEY AUTO_INCREMENT,
//name VARCHAR(50) NOT NULL UNIQUE
//);
//
//CREATE TABLE users (
//id BIGINT PRIMARY KEY AUTO_INCREMENT,
//username VARCHAR(50) NOT NULL UNIQUE,
//password VARCHAR(255) NOT NULL,
//enabled BOOLEAN DEFAULT TRUE,
//role_id BIGINT NOT NULL,
//
//
//CONSTRAINT fk_user_role
//    FOREIGN KEY(role_id)
//    REFERENCES roles(id)
//);
//
//INSERT INTO roles(name) VALUES ('ROLE_ADMIN');
//INSERT INTO roles(name) VALUES ('ROLE_USER');