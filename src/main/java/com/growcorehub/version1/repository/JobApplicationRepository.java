package com.growcorehub.version1.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.growcorehub.version1.entity.JobApplication;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

	List<JobApplication> findByUserIdOrderByAppliedAtDesc(Long userId);

	List<JobApplication> findByJobIdOrderByAppliedAtDesc(Long jobId);

	Optional<JobApplication> findByUserIdAndJobId(Long userId, Long jobId);

	@Query("SELECT COUNT(ja) FROM JobApplication ja WHERE ja.user.id = :userId")
	Long countApplicationsByUserId(@Param("userId") Long userId);

	List<JobApplication> findByStatus(String status);
}