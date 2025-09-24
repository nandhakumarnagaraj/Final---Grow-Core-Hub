package com.growcorehub.version1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.growcorehub.version1.entity.Job;
import com.growcorehub.version1.entity.JobApplication;
import com.growcorehub.version1.service.JobApplicationService;
import com.growcorehub.version1.service.JobService;
import com.growcorehub.version1.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

	private final JobService jobService;
	private final UserService userService;

	public JobController(JobService jobService, UserService userService) {
		this.jobService = jobService;
		this.userService = userService;
	}

	@GetMapping("/")
	public List<Job> getAllJobs(@RequestParam(required = false) String status,
			@RequestParam(required = false) String location, @RequestParam(required = false) String search) {

		if (search != null && !search.trim().isEmpty()) {
			return jobService.searchJobs(search);
		}

		if (location != null && !location.trim().isEmpty()) {
			return jobService.getJobsByLocation(location);
		}

		return jobService.getJobsByStatus(status != null ? status : "OPEN");
	}

	@GetMapping("/{id}")
	public ResponseEntity<Job> getJobById(@PathVariable Long id) {
		Optional<Job> job = jobService.getJobById(id);
		return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/")
	public ResponseEntity<Job> createJob(@RequestBody Job job, Authentication authentication) {
		String username = authentication.getName();
		var user = userService.findByUsername(username);

		if (user.isPresent()) {
			job.setCreatedBy(user.get());
			if (job.getStatus() == null) {
				job.setStatus("OPEN");
			}
			Job savedJob = jobService.saveJob(job);
			return ResponseEntity.ok(savedJob);
		}

		return ResponseEntity.badRequest().build();
	}

	@GetMapping("/my-jobs")
	public ResponseEntity<List<Job>> getMyJobs(Authentication authentication) {
		String username = authentication.getName();
		var user = userService.findByUsername(username);

		if (user.isPresent()) {
			List<Job> myJobs = jobService.getJobsByCreatedBy(user.get().getId());
			return ResponseEntity.ok(myJobs);
		}

		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job jobDetails,
			Authentication authentication) {
		String username = authentication.getName();
		var user = userService.findByUsername(username);

		if (user.isPresent()) {
			Optional<Job> existingJob = jobService.getJobById(id);

			if (existingJob.isPresent() && existingJob.get().getCreatedBy().getId().equals(user.get().getId())) {
				Job job = existingJob.get();
				job.setTitle(jobDetails.getTitle());
				job.setDescription(jobDetails.getDescription());
				job.setLocation(jobDetails.getLocation());
				job.setStatus(jobDetails.getStatus());

				Job updatedJob = jobService.saveJob(job);
				return ResponseEntity.ok(updatedJob);
			}
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteJob(@PathVariable Long id, Authentication authentication) {
		String username = authentication.getName();
		var user = userService.findByUsername(username);

		if (user.isPresent()) {
			Optional<Job> existingJob = jobService.getJobById(id);

			if (existingJob.isPresent() && existingJob.get().getCreatedBy().getId().equals(user.get().getId())) {
				jobService.deleteJob(id);
				return ResponseEntity.noContent().build();
			}
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping("/apply/{jobId}")
	public ResponseEntity<JobApplication> applyForJob(@PathVariable Long jobId,
			@RequestBody(required = false) JobApplication applicationRequest, Authentication authentication) {
		String username = authentication.getName();
		Optional<User> user = userService.findByUsername(username);
		Optional<Job> job = jobService.getJobById(jobId);

		if (user.isPresent() && job.isPresent()) {
			// Check if user already applied
			Optional<JobApplication> existingApplication = JobApplicationService
					.getApplicationByUserAndJob(user.get().getId(), jobId);

			if (existingApplication.isPresent()) {
				return ResponseEntity.badRequest().build(); // Already applied
			}

			JobApplication application = new JobApplication();
			application.setUser(user.get());
			application.setJob(job.get());
			application.setMessage(applicationRequest != null ? applicationRequest.getMessage() : "");

			JobApplication savedApplication = jobApplicationService.applyForJob(application);
			return ResponseEntity.ok(savedApplication);
		}

		return ResponseEntity.notFound().build();
	}

}