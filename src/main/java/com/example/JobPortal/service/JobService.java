package com.example.JobPortal.service;

import com.example.JobPortal.entity.Job;
import com.example.JobPortal.entity.JobApplication;
import com.example.JobPortal.repository.JobApplicationRepository;
import com.example.JobPortal.repository.JobRepository;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service

public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private EmailService emailService;

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public JobApplication applyForJob(Long jobId, JobApplication application) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        application.setJob(job);
        application.setAppliedDate(LocalDateTime.now());
        JobApplication savedApplication = applicationRepository.save(application);

        // Send notification via JMS
        jmsTemplate.convertAndSend("jobApplicationQueue",
                Map.of("applicantEmail", application.getApplicantEmail(),
                        "jobTitle", job.getTitle()));

        return savedApplication;
    }

    public List<Job> filterJobs(String city, String skills, String level, String department) {
        return jobRepository.findByActiveTrueAndCityAndSkillsContainingAndLevelAndDepartment(
                city, skills, level, department);
    }

    public Job updateJob(Long jobId, Job jobDetails) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setTitle(jobDetails.getTitle());
        job.setDescription(jobDetails.getDescription());
        job.setCity(jobDetails.getCity());
        job.setSkills(jobDetails.getSkills());
        job.setLevel(jobDetails.getLevel());
        job.setDepartment(jobDetails.getDepartment());

        return jobRepository.save(job);
    }

    public void deleteJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setActive(false);
        jobRepository.save(job);
    }

    public void withdrawApplication(Long applicationId) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setActive(false);
        applicationRepository.save(application);

        // Notify via email
        emailService.sendEmail(
                application.getApplicantEmail(),
                "Job Application Withdrawn",
                "Your application for " + application.getJob().getTitle() + " has been withdrawn."
        );
    }

    public JobApplication updateApplication(Long applicationId, JobApplication applicationDetails) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setApplicantName(applicationDetails.getApplicantName());
        application.setResume(applicationDetails.getResume());

        JobApplication updatedApplication = applicationRepository.save(application);

        // Notify via email
        emailService.sendEmail(
                application.getApplicantEmail(),
                "Job Application Updated",
                "Your application for " + application.getJob().getTitle() + " has been updated."
        );

        return updatedApplication;
    }
}
