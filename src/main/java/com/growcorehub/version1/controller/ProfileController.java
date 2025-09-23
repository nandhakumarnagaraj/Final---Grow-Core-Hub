package com.growcorehub.version1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.growcorehub.version1.entity.Profile;
import com.growcorehub.version1.entity.User;
import com.growcorehub.version1.service.ProfileService;
import com.growcorehub.version1.service.UserService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

	private final ProfileService profileService;
	private final UserService userService;

	public ProfileController(ProfileService profileService, UserService userService) {
		this.profileService = profileService;
		this.userService = userService;
	}

	@GetMapping("/")
	public ResponseEntity<Profile> getUserProfile(Authentication authentication) {
		String username = authentication.getName();
		var user = userService.findByUsername(username);

		if (user.isPresent()) {
			Profile profile = profileService.getProfileByUserId(user.get().getId());
			return ResponseEntity.ok(profile);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/")
	public ResponseEntity<Profile> createOrUpdateProfile(@RequestBody Profile profile, Authentication authentication) {
		String username = authentication.getName();
		var user = userService.findByUsername(username);

		if (user.isPresent()) {
			profile.setUser(user.get());
			Profile savedProfile = profileService.saveProfile(profile);
			return ResponseEntity.ok(savedProfile);
		}
		return ResponseEntity.badRequest().build();
	}

	@PutMapping("/")
	public ResponseEntity<Profile> updateProfile(@RequestBody Profile profile, Authentication authentication) {
		String username = authentication.getName();
		var user = userService.findByUsername(username);

		if (user.isPresent()) {
			Profile existingProfile = profileService.getProfileByUserId(user.get().getId());
			if (existingProfile != null) {
				existingProfile.setDetails(profile.getDetails());
				Profile updatedProfile = profileService.saveProfile(existingProfile);
				return ResponseEntity.ok(updatedProfile);
			} else {
				profile.setUser(user.get());
				Profile newProfile = profileService.saveProfile(profile);
				return ResponseEntity.ok(newProfile);
			}
		}
		return ResponseEntity.badRequest().build();
	}

	@PutMapping("/user-info")
	public ResponseEntity<User> updateUserInfo(@RequestBody User userInfo, Authentication authentication) {
		String username = authentication.getName();
		var user = userService.findByUsername(username);

		if (user.isPresent()) {
			User existingUser = user.get();
			if (userInfo.getFullName() != null) {
				existingUser.setFullName(userInfo.getFullName());
			}
			if (userInfo.getEmail() != null) {
				existingUser.setEmail(userInfo.getEmail());
			}
			if (userInfo.getProfessionalDetails() != null) {
				existingUser.setProfessionalDetails(userInfo.getProfessionalDetails());
			}

			User updatedUser = userService.saveUser(existingUser);
			// Remove password from response
			updatedUser.setPassword(null);
			return ResponseEntity.ok(updatedUser);
		}
		return ResponseEntity.badRequest().build();
	}
}