package com.growcorehub.version1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.growcorehub.version1.dto.DashboardDTO;
import com.growcorehub.version1.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/")
	public ResponseEntity<DashboardDTO> getDashboardData(Authentication authentication) {
		String username = authentication.getName();
		DashboardDTO dashboardData = dashboardService.getDashboardData(username);
		return ResponseEntity.ok(dashboardData);
	}

	@GetMapping("/stats")
	public ResponseEntity<DashboardDTO.StatsDTO> getUserStats(Authentication authentication) {
		String username = authentication.getName();
		DashboardDTO.StatsDTO stats = dashboardService.getUserStats(username);
		return ResponseEntity.ok(stats);
	}
}