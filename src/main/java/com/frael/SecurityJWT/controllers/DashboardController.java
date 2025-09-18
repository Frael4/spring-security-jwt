package com.frael.SecurityJWT.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    
    @GetMapping("/modulo1")
    public String dashboard() {
        return "This method is not secured";
    }

    @GetMapping("/modulo2")
    public String dashboard2() {
        return "This method is secured";
    }

    @GetMapping("/menu2")
    public String menu2() {
        return "Endpoint accessed by USER or ADMIN";
    }
}
