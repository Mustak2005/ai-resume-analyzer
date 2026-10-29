package com.mustak.resumeanalyzer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mustak.resumeanalyzer.entity.Analysis;
import com.mustak.resumeanalyzer.service.AnalysisService;

@RestController
@RequestMapping("/api/analysis")
public class HistoryController {


private final AnalysisService service;

public HistoryController(AnalysisService service) {
    this.service = service;
}

@GetMapping("/history")
public ResponseEntity<List<Analysis>> history() {
    return ResponseEntity.ok(service.history());
}

@GetMapping("/{id}")
public ResponseEntity<?> get(@PathVariable Long id) {

    Optional<Analysis> analysis = service.history()
            .stream()
            .filter(a -> a.getId() != null && a.getId().equals(id))
            .findFirst();

    if (analysis.isPresent()) {
        return ResponseEntity.ok(analysis.get());
    }

    return ResponseEntity.notFound().build();
}


}
