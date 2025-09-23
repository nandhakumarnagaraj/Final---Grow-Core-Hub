package com.growcorehub.version1.dto;

import lombok.Data;

@Data
public class JobResponseDTO {
	private Long id;
	private String title;
	private String description;
	private String location;
	private String status;
	private UserSummaryDTO createdBy;

	@Data
	public static class UserSummaryDTO {
		private Long id;
		private String username;
		private String fullName;
		private String email;
	}
}