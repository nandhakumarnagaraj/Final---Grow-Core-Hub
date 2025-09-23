package com.growcorehub.version1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.growcorehub.version1.entity.Certification;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {

	List<Certification> findByUserId(Long userId);

}
