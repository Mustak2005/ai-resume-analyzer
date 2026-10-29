package com.mustak.resumeanalyzer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analyses")
public class Analysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resumeFileName;
    private Integer resumeScore;
    private Integer matchPercentage;

    @Column(length = 5000)
    private String jobDescription;

    @Column(length = 5000)
    private String matchingSkills;

    @Column(length = 5000)
    private String missingSkills;

    @Column(length = 5000)
    private String strengths;

    @Column(length = 5000)
    private String recommendations;

    private LocalDateTime createdAt;

    public Analysis() {}

    @PrePersist
    public void beforeSave() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getResumeFileName() { return resumeFileName; }
    public void setResumeFileName(String v) { resumeFileName = v; }
    public Integer getResumeScore() { return resumeScore; }
    public void setResumeScore(Integer v) { resumeScore = v; }
    public Integer getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(Integer v) { matchPercentage = v; }
    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String v) { jobDescription = v; }
    public String getMatchingSkills() { return matchingSkills; }
    public void setMatchingSkills(String v) { matchingSkills = v; }
    public String getMissingSkills() { return missingSkills; }
    public void setMissingSkills(String v) { missingSkills = v; }
    public String getStrengths() { return strengths; }
    public void setStrengths(String v) { strengths = v; }
    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String v) { recommendations = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
