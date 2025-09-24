package com.growcorehub.version1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping
public class HealthController {

	@GetMapping("/health")
	public ResponseEntity<Map<String, String>> health() {
		Map<String, String> status = new HashMap<>();
		status.put("status", "UP");
		status.put("timestamp", java.time.Instant.now().toString());
		return ResponseEntity.ok(status);
	}
}