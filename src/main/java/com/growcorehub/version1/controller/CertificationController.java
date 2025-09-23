package com.growcorehub.version1.controller;

import org.springframework.web.bind.annotation.*;

import com.growcorehub.version1.entity.Certification;
import com.growcorehub.version1.service.CertificationService;

import java.util.List;

@RestController
@RequestMapping("/api/certifications")
public class CertificationController {
	
	private final CertificationService certificationService;

	public CertificationController(CertificationService certificationService) {
		this.certificationService = certificationService;
	}

	@GetMapping("/user/{userId}")
	public List<Certification> getCertificationsByUser(@PathVariable Long userId) {
		return certificationService.getCertificationsByUserId(userId);
	}

	@PostMapping("/")
	public Certification addCertification(@RequestBody Certification certification) {
		return certificationService.saveCertification(certification);
	}
}
