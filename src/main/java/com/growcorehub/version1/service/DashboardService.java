package com.growcorehub.version1.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.growcorehub.version1.dto.DashboardDTO;
import com.growcorehub.version1.dto.DashboardDTO.CertificationProgressDTO;
import com.growcorehub.version1.dto.DashboardDTO.RecentJobDTO;
import com.growcorehub.version1.dto.DashboardDTO.RecentPaymentDTO;
import com.growcorehub.version1.dto.DashboardDTO.StatsDTO;
import com.growcorehub.version1.dto.DashboardDTO.UserSummaryDTO;
import com.growcorehub.version1.entity.Certification;
import com.growcorehub.version1.entity.Payment;
import com.growcorehub.version1.entity.User;
import com.growcorehub.version1.repository.CertificationRepository;
import com.growcorehub.version1.repository.PaymentRepository;
import com.growcorehub.version1.repository.UserRepository;

@Service
public class DashboardService {

	private final UserRepository userRepository;
	private final CertificationRepository certificationRepository;
	private final PaymentRepository paymentRepository;

	public DashboardService(UserRepository userRepository, CertificationRepository certificationRepository,
			PaymentRepository paymentRepository) {
		this.userRepository = userRepository;
		this.certificationRepository = certificationRepository;
		this.paymentRepository = paymentRepository;
	}

	public DashboardDTO getDashboardData(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found: " + username));

		DashboardDTO dashboard = new DashboardDTO();

		// User Summary
		UserSummaryDTO userSummary = new UserSummaryDTO(user.getId(), user.getUsername(),
				user.getFullName() != null ? user.getFullName() : user.getUsername(), user.getEmail(),
				user.getProfessionalDetails());
		dashboard.setUserSummary(userSummary);

		// Stats
		dashboard.setStats(getUserStats(username));

		// Recent Jobs - Since we don't have JobApplication entity in your current
		// setup,
		// we'll create a simple implementation. You can enhance this later.
		List<RecentJobDTO> recentJobs = List.of(new RecentJobDTO(1L, "Sample Job 1", "Remote", "PENDING"),
				new RecentJobDTO(2L, "Sample Job 2", "New York", "APPLIED"));
		dashboard.setRecentJobs(recentJobs);

		// Certification Progress
		List<Certification> certifications = certificationRepository.findByUserId(user.getId());
		List<CertificationProgressDTO> certProgress = certifications.stream()
				.map(cert -> new CertificationProgressDTO(cert.getId(), cert.getName(),
						cert.getUserProgress() != null ? cert.getUserProgress() : 0.0))
				.collect(Collectors.toList());
		dashboard.setCertificationProgress(certProgress);

		// Recent Payments
		List<Payment> payments = paymentRepository.findByUserId(user.getId());
		List<RecentPaymentDTO> recentPayments = payments.stream().limit(5) // Get only last 5 payments
				.map(payment -> new RecentPaymentDTO(payment.getId(), payment.getAmount(), payment.getStatus(),
						payment.getTransactionDate() != null
								? payment.getTransactionDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
								: "N/A"))
				.collect(Collectors.toList());
		dashboard.setRecentPayments(recentPayments);

		return dashboard;
	}

	public StatsDTO getUserStats(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found: " + username));

		// Get certifications count
		List<Certification> certifications = certificationRepository.findByUserId(user.getId());
		Long totalCertifications = (long) certifications.size();

		// Calculate total earnings from completed payments
		List<Payment> payments = paymentRepository.findByUserId(user.getId());
		Double totalEarnings = payments.stream()
				.filter(payment -> "COMPLETED".equals(payment.getStatus()) || "PAID".equals(payment.getStatus()))
				.mapToDouble(Payment::getAmount).sum();

		// Calculate average certification progress
		Double avgCertificationProgress = certifications.stream()
				.mapToDouble(cert -> cert.getUserProgress() != null ? cert.getUserProgress() : 0.0).average()
				.orElse(0.0);

		// Count completed projects (payments with COMPLETED status)
		Long completedProjects = payments.stream().filter(payment -> "COMPLETED".equals(payment.getStatus()))
				.mapToLong(p -> 1L).sum();

		// For jobs applied, we'll use a simple count for now
		// You can replace this with actual job application count when you implement
		// that feature
		Long totalJobsApplied = 5L; // Placeholder - replace with actual job application count

		return new StatsDTO(totalJobsApplied, totalCertifications, totalEarnings, completedProjects,
				Math.round(avgCertificationProgress * 100.0) / 100.0 // Round to 2 decimal places
		);
	}
}