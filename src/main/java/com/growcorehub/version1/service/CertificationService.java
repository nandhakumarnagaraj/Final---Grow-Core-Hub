package com.growcorehub.version1.service;

import org.springframework.stereotype.Service;

import com.growcorehub.version1.entity.Certification;
import com.growcorehub.version1.repository.CertificationRepository;

import java.util.List;

@Service
public class CertificationService {
	private final CertificationRepository certificationRepository;

	public CertificationService(CertificationRepository certificationRepository) {
		this.certificationRepository = certificationRepository;
	}

	public List<Certification> getCertificationsByUserId(Long userId) {
		return certificationRepository.findByUserId(userId);
	}

	public Certification saveCertification(Certification certification) {
		return certificationRepository.save(certification);
	}
}
