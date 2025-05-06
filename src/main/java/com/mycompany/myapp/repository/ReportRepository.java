package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
