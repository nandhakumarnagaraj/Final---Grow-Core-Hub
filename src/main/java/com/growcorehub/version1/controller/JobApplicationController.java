package com.growcorehub.version1.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.growcorehub.version1.entity.Job;
import com.growcorehub.version1.entity.JobApplication;
import com.growcorehub.version1.entity.User;
import com.growcorehub.version1.service.JobApplicationService;
import com.growcorehub.version1.service.JobService;
import com.growcorehub.version1.service.UserService;

@RestController
@RequestMapping("/api/job-applications")
public class JobApplicationController {

	private final JobApplicationService jobApplicationService;
	private final UserService userService;
	private final JobService jobService;

	public JobApplicationController(JobApplicationService jobApplicationService, UserService userService,
			JobService jobService) {
		this.jobApplicationService = jobApplicationService;
		this.userService = userService;
		this.jobService = jobService;
	}

	// APPLY FOR JOB METHOD
	@PostMapping("/apply/{jobId}")
	public ResponseEntity<JobApplication> applyForJob(@PathVariable Long jobId,
			@RequestBody(required = false) JobApplication applicationRequest, Authentication authentication) {
		try {
			String username = authentication.getName();
			Optional<User> user = userService.findByUsername(username);
			Optional<Job> job = jobService.getJobById(jobId);

			// Validation
			if (!user.isPresent()) {
				return ResponseEntity.status(401).build(); // Unauthorized
			}

			if (!job.isPresent()) {
				return ResponseEntity.notFound().build(); // Job not found
			}

			// Check if user is trying to apply to their own job
			if (job.get().getCreatedBy().getId().equals(user.get().getId())) {
				return ResponseEntity.badRequest().build(); // Can't apply to own job
			}

			// Check if user already applied
			Optional<JobApplication> existingApplication = jobApplicationService
					.getApplicationByUserAndJob(user.get().getId(), jobId);

			if (existingApplication.isPresent()) {
				return ResponseEntity.status(409).build(); // Conflict - Already applied
			}

			// Create new application
			JobApplication application = new JobApplication();
			application.setUser(user.get());
			application.setJob(job.get());
			application.setMessage(applicationRequest != null ? applicationRequest.getMessage() : "");

			JobApplication savedApplication = jobApplicationService.applyForJob(application);
			return ResponseEntity.ok(savedApplication);

		} catch (Exception e) {
			return ResponseEntity.status(500).build(); // Internal server error
		}
	}

	// GET MY APPLICATIONS
	@GetMapping("/my-applications")
	public ResponseEntity<List<JobApplication>> getMyApplications(Authentication authentication) {
		String username = authentication.getName();
		Optional<User> user = userService.findByUsername(username);

		if (user.isPresent()) {
			List<JobApplication> applications = jobApplicationService.getApplicationsByUserId(user.get().getId());
			return ResponseEntity.ok(applications);
		}

		return ResponseEntity.notFound().build();
	}

// GET APPLICATIONS FOR MY JOB (Job Creator Only)
	@GetMapping("/job/{jobId}")
	public ResponseEntity<List<JobApplication>> getApplicationsForJob(@PathVariable Long jobId,
			Authentication authentication) {

		String username = authentication.getName();
		Optional<User> user = userService.findByUsername(username);
		Optional<Job> job = jobService.getJobById(jobId);

		if (user.isPresent() && job.isPresent()) {
			// Check if user is the job creator
			if (job.get().getCreatedBy().getId().equals(user.get().getId())) {
				List<JobApplication> applications = jobApplicationService.getApplicationsByJobId(jobId);
				return ResponseEntity.ok(applications);
			} else {
				// Forbidden: user is not the job creator
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		}

		// Either user or job not found
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	// UPDATE APPLICATION STATUS (Job Creator Only)
	@PutMapping("/{applicationId}/status")
	public ResponseEntity<JobApplication> updateApplicationStatus(@PathVariable Long applicationId,
			@RequestBody Map<String, String> statusUpdate, Authentication authentication) {
		String username = authentication.getName();
		Optional<User> user = userService.findByUsername(username);

		if (user.isPresent()) {
			try {
				String status = statusUpdate.get("status");
				JobApplication updatedApplication = jobApplicationService.updateApplicationStatus(applicationId,
						status);
				return ResponseEntity.ok(updatedApplication);
			} catch (RuntimeException e) {
				return ResponseEntity.notFound().build();
			}
		}

		return ResponseEntity.badRequest().build();
	}

	// WITHDRAW APPLICATION
	@DeleteMapping("/{applicationId}")
	public ResponseEntity<Void> deleteApplication(@PathVariable Long applicationId, Authentication authentication) {
		String username = authentication.getName();
		Optional<User> user = userService.findByUsername(username);

		if (user.isPresent()) {
			jobApplicationService.deleteApplication(applicationId);
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.badRequest().build();
	}
}