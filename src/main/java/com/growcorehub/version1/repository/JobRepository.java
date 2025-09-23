package com.growcorehub.version1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.growcorehub.version1.entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

	List<Job> findByStatus(String status);

}
