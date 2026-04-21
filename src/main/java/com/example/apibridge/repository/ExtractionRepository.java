package com.example.apibridge.repository;

import com.example.apibridge.model.Extraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtractionRepository extends JpaRepository<Extraction, Long> {
}
