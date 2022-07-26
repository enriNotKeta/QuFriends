package app.service;


import app.exception.BadResourceException;
import app.exception.ResourceAlreadyExistsException;
import app.model.Relationship;
import app.model.User;
import app.model.UserReport;
import app.repository.UserReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserReportService {
    private final UserReportRepository userReportRepository;
    private final UserService userService;


    @Autowired
    public UserReportService(UserReportRepository userReportRepository, UserService userService) {
        this.userReportRepository = userReportRepository;
        this.userService = userService;
    }

    public UserReport save(UserReport userReport, Long reportedUserId) throws BadResourceException, ResourceAlreadyExistsException {
        if (userReport.getId() != null && existsById(userReport.getId())) {
            throw new ResourceAlreadyExistsException("User Report with id: " + userReport.getId() +
                    " already exists");
        }

        userReport.setReportedUser(userService.getUserById(reportedUserId));
        userReport.setReporterUser(userService.getCurrentUser());

        return userReportRepository.save(userReport);
    }

    public boolean existsById(Long id) {
        return userReportRepository.existsById(id);
    }


    public List<UserReport> getReports() {
        List<UserReport> userReports = userReportRepository.getReports();
        return userReports;
    }

    public UserReport ignoreUserReport(Long userReportId) {
        Optional<UserReport> fetchUserReport = userReportRepository.findById(userReportId);
        UserReport userReport = fetchUserReport.orElseThrow(() ->
                new ResourceNotFoundException("User Report with ID: " + userReportId+ " doesnt exist"));;
        userReport.setIncorrect(true);
        return userReportRepository.save(userReport);
    }



}
