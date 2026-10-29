package com.mustak.resumeanalyzer.repository;

import com.mustak.resumeanalyzer.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
}
