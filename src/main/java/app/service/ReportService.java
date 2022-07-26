package app.service;

import app.exception.BadResourceException;
import app.exception.ResourceAlreadyExistsException;
import app.model.Relationship;
import app.model.Report;
import app.model.User;
import app.model.UserReport;
import app.repository.RelationshipRepository;
import app.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report findById(Long id) throws ResourceNotFoundException {
        Report report = reportRepository.findById(id).orElse(null);
        if (report==null) {
            throw new ResourceNotFoundException("Cannot find report with id: " + id);
        }
        else return report;
    }

    public List<Report> findAll() {
        List<Report> reports = new ArrayList<>();
        reportRepository.findAll().forEach(reports::add);
        return reports;
    }

    public Report save(Report report) throws ResourceAlreadyExistsException {
        if (report.getId() != null && existsById(report.getId())) {
            throw new ResourceAlreadyExistsException("Report with id: " + report.getId() +
                    " already exists");
        }

        return reportRepository.save(report);
    }

    public boolean existsById(Long id) {
        return reportRepository.existsById(id);
    }





}
