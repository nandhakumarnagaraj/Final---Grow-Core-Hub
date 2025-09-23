package com.growcorehub.version1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "certifications")
public class Certification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	private Double userProgress;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Certification(Long id, String name, String description, Double userProgress, User user) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.userProgress = userProgress;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getUserProgress() {
		return userProgress;
	}

	public void setUserProgress(Double userProgress) {
		this.userProgress = userProgress;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Certification() {
		super();
	}

}
