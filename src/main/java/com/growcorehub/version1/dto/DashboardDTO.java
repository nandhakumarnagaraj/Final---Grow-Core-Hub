package com.growcorehub.version1.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
	private UserSummaryDTO userSummary;
	private StatsDTO stats;
	private List<RecentJobDTO> recentJobs;
	private List<CertificationProgressDTO> certificationProgress;
	private List<RecentPaymentDTO> recentPayments;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserSummaryDTO {
		private Long id;
		private String username;
		private String fullName;
		private String email;
		private String professionalDetails;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class StatsDTO {
		private Long totalJobsApplied;
		private Long totalCertifications;
		private Double totalEarnings;
		private Long completedProjects;
		private Double certificationProgress;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RecentJobDTO {
		private Long id;
		private String title;
		private String location;
		private String status;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CertificationProgressDTO {
		private Long id;
		private String name;
		private Double progress;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RecentPaymentDTO {
		private Long id;
		private Double amount;
		private String status;
		private String transactionDate;
	}
}