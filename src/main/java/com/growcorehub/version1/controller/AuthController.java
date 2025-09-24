package com.growcorehub.version1.controller;

import com.growcorehub.version1.entity.User;
import com.growcorehub.version1.dto.JwtResponse;
import com.growcorehub.version1.dto.LoginRequest;
import com.growcorehub.version1.entity.Role;
import com.growcorehub.version1.security.JwtTokenProvider;
import com.growcorehub.version1.service.UserService;
import com.growcorehub.version1.util.PasswordEncoderUtil;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth") 
public class AuthController {

	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;

	public AuthController(UserService userService, AuthenticationManager authenticationManager,
			JwtTokenProvider tokenProvider) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
		// Check if username already exists
		if (userService.findByUsername(user.getUsername()).isPresent()) {
			return ResponseEntity.badRequest().body("Username is already taken!");
		}

		// Check if email already exists
		if (userService.findByEmail(user.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().body("Email is already in use!");
		}

		// encode password
		user.setPassword(PasswordEncoderUtil.encodePassword(user.getPassword()));

		// default role if none provided
		if (user.getRoles() == null || user.getRoles().isEmpty()) {
			user.setRoles(Set.of(Role.FREELANCER));
		}

		userService.saveUser(user);
		return ResponseEntity.ok("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			String token = tokenProvider.generateToken(loginRequest.getUsername());
			return ResponseEntity.ok(new JwtResponse(token));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(401).body("Invalid username or password");
		}
	}
}
