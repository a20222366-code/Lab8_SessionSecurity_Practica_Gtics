package org.example.lab8_gtics_practica_session_security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "login/form";
    }
}