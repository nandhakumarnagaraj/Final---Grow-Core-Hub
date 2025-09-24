package com.growcorehub.version1.service;

import java.util.List;
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

	public List<JobApplication> getApplicationsByUserId(Long userId) {
		return jobApplicationRepository.findByUserIdOrderByAppliedAtDesc(userId);
	}

	public List<JobApplication> getApplicationsByJobId(Long jobId) {
		return jobApplicationRepository.findByJobIdOrderByAppliedAtDesc(jobId);
	}

	public Optional<JobApplication> getApplicationByUserAndJob(Long userId, Long jobId) {
		return jobApplicationRepository.findByUserIdAndJobId(userId, jobId);
	}

	public JobApplication updateApplicationStatus(Long applicationId, String status) {
		Optional<JobApplication> application = jobApplicationRepository.findById(applicationId);
		if (application.isPresent()) {
			JobApplication app = application.get();
			app.setStatus(status);
			return jobApplicationRepository.save(app);
		}
		throw new RuntimeException("Application not found");
	}

	public Long getTotalApplicationsByUser(Long userId) {
		return jobApplicationRepository.countApplicationsByUserId(userId);
	}

	public void deleteApplication(Long applicationId) {
		jobApplicationRepository.deleteById(applicationId);
	}
}