package com.growcorehub.version1.service;

import org.springframework.stereotype.Service;
import com.growcorehub.version1.entity.Profile;
import com.growcorehub.version1.repository.ProfileRepository;

@Service
public class ProfileService {

	private final ProfileRepository profileRepository;

	public ProfileService(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	public Profile getProfileByUserId(Long userId) {
		return profileRepository.findByUserId(userId).orElse(null);
	}

	public Profile saveProfile(Profile profile) {
		return profileRepository.save(profile);
	}

	public void deleteProfile(Long profileId) {
		profileRepository.deleteById(profileId);
	}
}