package com.growcorehub.version1.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.growcorehub.version1.entity.JobApplication;
import com.growcorehub.version1.repository.JobApplicationRepository;
import com.growcorehub.version1.exception.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class JobApplicationService {

	private final JobApplicationRepository jobApplicationRepository;

	public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
		this.jobApplicationRepository = jobApplicationRepository;
	}

	@Transactional
	public JobApplication applyForJob(JobApplication jobApplication) {
		return jobApplicationRepository.save(jobApplication);
	}

	public List<JobApplication> getApplicationsByUserId(Long userId) {
		return jobApplicationRepository.findByUserIdOrderByAppliedAtDesc(userId);
	}

	public List<JobApplication> getApplicationsByJobId(Long jobId) {
		return jobApplicationRepository.findByJobIdOrderByAppliedAtDesc(jobId);
	}

	public Optional<JobApplication> getApplicationByUserAndJob(Long userId, Long jobId) {
		return jobApplicationRepository.findByUserIdAndJobId(userId, jobId);
	}

	@Transactional
	public JobApplication updateApplicationStatus(Long applicationId, String status) {
		JobApplication application = jobApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

		application.setStatus(status);
		return jobApplicationRepository.save(application);
	}

	public Long getTotalApplicationsByUser(Long userId) {
		return jobApplicationRepository.countApplicationsByUserId(userId);
	}

	@Transactional
	public void deleteApplication(Long applicationId) {
		if (!jobApplicationRepository.existsById(applicationId)) {
			throw new ResourceNotFoundException("Application not found with id: " + applicationId);
		}
		jobApplicationRepository.deleteById(applicationId);
	}
}