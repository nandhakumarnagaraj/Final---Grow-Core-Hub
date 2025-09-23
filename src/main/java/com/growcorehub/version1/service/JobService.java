package com.growcorehub.version1.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.growcorehub.version1.entity.Job;
import com.growcorehub.version1.repository.JobRepository;

@Service
public class JobService {
	private final JobRepository jobRepository;

	public JobService(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	public List<Job> getJobsByStatus(String status) {
		return jobRepository.findByStatus(status);
	}

	public Optional<Job> getJobById(Long id) {
		return jobRepository.findById(id);
	}

	public Job saveJob(Job job) {
		return jobRepository.save(job);
	}

	public void deleteJob(Long id) {
		jobRepository.deleteById(id);
	}

	// Missing methods required by JobController
	public List<Job> searchJobs(String searchTerm) {
		return jobRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm);
	}

	public List<Job> getJobsByLocation(String location) {
		return jobRepository.findByLocationContainingIgnoreCase(location);
	}

	public List<Job> getJobsByCreatedBy(Long userId) {
		return jobRepository.findByCreatedByIdOrderByIdDesc(userId);
	}
}