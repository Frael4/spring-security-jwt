package com.frael.SecurityJWT;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.frael.SecurityJWT.models.ERole;
import com.frael.SecurityJWT.models.RoleEntity;
import com.frael.SecurityJWT.models.UserEntity;
import com.frael.SecurityJWT.repository.UserRepository;

@SpringBootApplication
public class SecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityJwtApplication.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserRepository userRepository;

	// Metodo para insertar usuarios en la BD cuando se ejecuta la app
	@Bean
	CommandLineRunner init() {

		return args -> {

			UserEntity user1 = UserEntity.builder().email("frael@email.com").username("frael")
					.password(passwordEncoder.encode("1234"))
					.roles(Set.of(RoleEntity.builder().name(ERole.ADMIN).build())).build();

			UserEntity user2 = UserEntity.builder().email("juan@email.com").username("juan")
					.password(passwordEncoder.encode("1234"))
					.roles(Set.of(RoleEntity.builder().name(ERole.USER).build())).build();

			userRepository.save(user1);
			userRepository.save(user2);
		};
	}

}
