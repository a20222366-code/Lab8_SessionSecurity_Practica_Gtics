package org.example.lab8_gtics_practica_session_security.config;

//Creamos model que es como un entity
//es donde ponemos los objetos que queremos guardar en la session

//UserHistoryController
//templates/user/history.html
//templates/user/history-detail.html

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

    //Cambiamos filterChain
    @Bean
    // Configuración principal del escudo de seguridad de la aplicación
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1. Configuración del Login personalizado
        http.formLogin(form -> form
                .loginPage("/loginForm")             // Muestra mi propio HTML de login en lugar del genérico de Spring
                .loginProcessingUrl("/processLogin") // Buzón invisible que procesa y valida las credenciales automáticamente
                .defaultSuccessUrl("/dashboard", true) // Si el usuario inicia sesión con éxito, lo manda directo al dashboard
                .permitAll());                       // Permite que todo el público pueda ver la pantalla de login

        // 2. Configuración del Logout (Cierre de sesión)
        http.logout(logout -> logout
                .logoutSuccessUrl("/loginForm?logout") // Al salir, lo regresa al login y le añade un aviso en la URL
                .deleteCookies("JSESSIONID", "SESSION") // Borra las "galletas" de identidad guardadas en el navegador
                .invalidateHttpSession(true));       // Elimina los datos de la sesión activa en el servidor/base de datos

        // 3. Control de accesos y permisos por Roles (Reglas de tránsito)
        http.authorizeHttpRequests(auth -> auth
                // Rutas públicas: cualquiera puede ver el login, el registro y las carpetas de diseño (CSS/JS)
                .requestMatchers("/loginForm", "/register", "/css/**", "/js/**").permitAll()
                // Control estricto: solo los usuarios con el rol oficial 'ROLE_ADMIN' entran a las rutas /admin
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Control estricto: solo los usuarios con el rol oficial 'ROLE_USER' entran a las rutas /user
                .requestMatchers("/user/**").hasRole("USER")
                // Candado general: cualquier otra ruta de la aplicación requiere estar logueado obligatoriamente
                .anyRequest().authenticated());

        // Construye y activa toda esta configuración en el contenedor de Spring
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



//USE minispotify_db;
//
//INSERT INTO users(username, password, enabled, role_id) VALUES
//('admin', '$2b$10$dzH29pi/OrkMWD81804RO.PbI7iAU.JCmKg.fSW4CWnCH4oDQUe3W', TRUE,
//    (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')),
//('user1', '$2b$10$KkpVlwpTGrROTbRV2UUDIOK1kQIYXuWHwZA8OeixQFBq7Uu7yD.2m', TRUE,
//    (SELECT id FROM roles WHERE name = 'ROLE_USER'));
//
//
//admin / admin123 (rol ADMIN)
//user1 / user123 (rol USER)