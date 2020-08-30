package com.bafagroupe.christab_security;

import com.bafagroupe.christab_security.entities.AppRole;
import com.bafagroupe.christab_security.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

@SpringBootApplication
public class ChristabSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChristabSecurityApplication.class, args);
	}

	@Bean
	BCryptPasswordEncoder geBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*@Bean
	CommandLineRunner start(AccountService accountService) {
		return args -> {
			accountService.saveRole(new AppRole(null, "ADMIN"));
			accountService.saveRole(new AppRole(null, "USER"));
			Stream.of("armande@mail.fr", "user1@mail.fr").forEach(u->{
				accountService.saveUser(u, "12345678", "12345678");
			});
			accountService.addRoleToUser("armande@mail.fr", "ADMIN");
		};
	}*/

}
