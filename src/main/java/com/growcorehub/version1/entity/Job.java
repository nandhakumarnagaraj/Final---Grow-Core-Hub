package com.growcorehub.version1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "jobs")
public class Job extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    @Column(nullable = false)
    private String title;

	@NotBlank(message = "Job description is required")
	@Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
	@Column(columnDefinition = "TEXT")
	private String description;

	@NotBlank(message = "Location is required")
	@Size(max = 100, message = "Location cannot exceed 100 characters")
	private String location;

    @Column(name = "status")
    @org.hibernate.annotations.Index(name = "idx_job_status")
    private String status = "OPEN";

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_by")
	@JsonIgnoreProperties({ "password", "roles" }) // Hide sensitive fields in JSON response
	private User createdBy;

	public Job(Long id, String title, String description, String location, String status, User createdBy) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.location = location;
		this.status = status;
		this.createdBy = createdBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Job() {
		super();
	}
}