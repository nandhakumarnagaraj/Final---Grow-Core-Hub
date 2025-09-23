package com.growcorehub.version1.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.growcorehub.version1.entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

	List<Job> findByStatus(String status);

	List<Job> findByCreatedByIdOrderByIdDesc(Long createdById);

	List<Job> findByLocationContainingIgnoreCase(String location);

	@Query("SELECT j FROM Job j WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	List<Job> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(@Param("searchTerm") String searchTerm);

	@Query("SELECT j FROM Job j WHERE j.status = 'OPEN' ORDER BY j.id DESC")
	List<Job> findOpenJobsOrderByIdDesc();
}