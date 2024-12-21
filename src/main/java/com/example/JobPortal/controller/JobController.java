package com.example.JobPortal.controller;

import com.example.JobPortal.entity.Job;
import com.example.JobPortal.entity.JobApplication;
import com.example.JobPortal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")

public class JobController {
    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        return ResponseEntity.ok(jobService.createJob(job));
    }

    @PostMapping("/{jobId}/apply")
    public ResponseEntity<JobApplication> applyForJob(
            @PathVariable Long jobId,
            @RequestBody JobApplication application) {
        return ResponseEntity.ok(jobService.applyForJob(jobId, application));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Job>> filterJobs(
            @RequestParam String city,
            @RequestParam String skills,
            @RequestParam String level,
            @RequestParam String department) {
        return ResponseEntity.ok(jobService.filterJobs(city, skills, level, department));
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<Job> updateJob(
            @PathVariable Long jobId,
            @RequestBody Job jobDetails) {
        return ResponseEntity.ok(jobService.updateJob(jobId, jobDetails));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/applications/{applicationId}")
    public ResponseEntity<Void> withdrawApplication(@PathVariable Long applicationId) {
        jobService.withdrawApplication(applicationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/applications/{applicationId}")
    public ResponseEntity<JobApplication> updateApplication(
            @PathVariable Long applicationId,
            @RequestBody JobApplication applicationDetails) {
        return ResponseEntity.ok(jobService.updateApplication(applicationId, applicationDetails));
    }
}
