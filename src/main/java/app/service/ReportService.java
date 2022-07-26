package app.service;

import app.model.Relationship;
import app.model.Report;
import app.model.User;
import app.repository.RelationshipRepository;
import app.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Report> findAll() {
        List<Report> reports = new ArrayList<>();
        reportRepository.findAll().forEach(reports::add);
        return reports;
    }}
