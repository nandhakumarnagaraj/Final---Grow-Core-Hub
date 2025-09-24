package com.growcorehub.version1.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.growcorehub.version1.entity.JobApplication;
import com.growcorehub.version1.repository.JobApplicationRepository;

@Service
public class JobApplicationService {

	private final JobApplicationRepository jobApplicationRepository;

	public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
		this.jobApplicationRepository = jobApplicationRepository;
	}

	public JobApplication applyForJob(JobApplication jobApplication) {
		return jobApplicationRepository.save(jobApplication);
	}

	public Optional<JobApplication> getApplicationByUserAndJob(Long userId, Long jobId) {
		return jobApplicationRepository.findByUserIdAndJobId(userId, jobId);
	}

	public void deleteApplication(Long applicationId) {
		// TODO Auto-generated method stub
		
	}
}
