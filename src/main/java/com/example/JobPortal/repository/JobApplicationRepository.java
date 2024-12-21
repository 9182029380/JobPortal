package com.example.JobPortal.repository;

import com.example.JobPortal.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobIdAndActiveTrue(Long jobId);
}
