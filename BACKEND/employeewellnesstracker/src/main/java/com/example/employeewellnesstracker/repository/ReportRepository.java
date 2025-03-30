package com.example.employeewellnesstracker.repository;

import com.example.employeewellnesstracker.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
