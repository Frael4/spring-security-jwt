package com.frael.SecurityJWT.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.frael.SecurityJWT.request.UserDTO;
import com.frael.SecurityJWT.models.UserEntity;
import com.frael.SecurityJWT.models.RoleEntity;
import com.frael.SecurityJWT.models.ERole;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import com.frael.SecurityJWT.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PrincipalController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/createUser")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {

        // Mapeo de los roles a la enumeracion ERole
        Set<RoleEntity> roles = userDTO.getRoles().stream()
                .map(role -> RoleEntity.builder().name(ERole.valueOf(role)).build()).collect(Collectors.toSet());

        UserEntity newUser = UserEntity.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles(roles)
                .build();

        UserEntity response = userRepository.save(newUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam String id) {
        userRepository.deleteById(Long.parseLong(id));

        return ResponseEntity.status(200).body("Usuario eliminado con id ".concat(id));
    }

}
