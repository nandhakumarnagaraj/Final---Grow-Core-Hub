package com.growcorehub.version1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.growcorehub.version1.entity.Job;
import com.growcorehub.version1.service.JobService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

	private final JobService jobService;

	public JobController(JobService jobService) {
		this.jobService = jobService;
	}

	@GetMapping("/")
	public List<Job> getAllJobs() {
		return jobService.getJobsByStatus("OPEN");
	}

	@GetMapping("/{id}")
	public ResponseEntity<Job> getJobById(@PathVariable Long id) {
		Optional<Job> job = jobService.getJobById(id);
		return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/")
	public Job createJob(@RequestBody Job job) {
		return jobService.saveJob(job);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
		jobService.deleteJob(id);
		return ResponseEntity.noContent().build();
	}
}
