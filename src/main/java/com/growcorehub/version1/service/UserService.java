package com.growcorehub.version1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.growcorehub.version1.entity.User;
import com.growcorehub.version1.repository.UserRepository;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Transactional
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Transactional
	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
	}
}
