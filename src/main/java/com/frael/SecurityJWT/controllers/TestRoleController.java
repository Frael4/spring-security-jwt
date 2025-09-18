package com.frael.SecurityJWT.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/role")
public class TestRoleController {

    @GetMapping("/accessAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accessAdmin() {
        return "Hola, has accedido con el rol de ADMIN";
    }

    @GetMapping("/accessUser")
    @PreAuthorize("hasRole('USER')")
    public String accessUser() {
        return "Hola, has accedido con el rol de USER";
    }

    @GetMapping("/accessAnyRole")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String accessAnyRole(Authentication authentication) {
        
        var name = authentication.getName();
        var role = authentication.getAuthorities().toString();

        return "Hola tu usuario es " + name + " y tu rol es: " + role;
    }

}
