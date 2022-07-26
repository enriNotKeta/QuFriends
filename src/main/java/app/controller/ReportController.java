package app.controller;

import app.model.Report;
import app.model.User;
import app.model.UserReport;
import app.repository.ReportRepository;
import app.repository.UserReportRepository;
import app.service.RelationshipService;
import app.service.ReportService;
import app.service.UserReportService;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class ReportController {
    private final UserService userService;
    private final ReportService reportService;
    private final UserReportService userReportService;


    @Autowired
    public ReportController(ReportService reportService, UserService userService,
                            UserReportService userReportService) {
        this.reportService = reportService;
        this.userService = userService;
        this.userReportService = userReportService;
    }


    @GetMapping(value = "/user/{userId}/report")
    public String showReport(Model model, @PathVariable long userId) {
        List<Report> reports = reportService.findAll();
        UserReport userReport = new UserReport();
        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("reports", reports);
        model.addAttribute("userreport", userReport);
        model.addAttribute("userId", userId);

        return "/user/report-send";
    }

    @PostMapping(value = "/user/{userId}/report")
    public String showReport(Model model, @ModelAttribute("userreport") UserReport userreport, @PathVariable long userId) {
        //fixed coz issues w thymeleaf
        Report report  = reportService.findById(1L);
        userreport.setReport(report);

        try {
            UserReport newUserReport = userReportService.save(userreport, userId);
            return "redirect:/matches";
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            model.addAttribute("errorMessage", errorMessage);

            List<Report> reports = reportService.findAll();
            model.addAttribute("currentUser", userService.getCurrentUser());
            model.addAttribute("reports", reports);
            model.addAttribute("userId", userId);
            return "/user/report-send";
        }
    }

    @PostMapping(value = "/report/add")
    public String addReport(Model model,
                           @ModelAttribute("report") Report report) {
        System.out.println(report.getTitle() + ", reporthere " + report.getDescription());
        try {
            reportService.save(report);
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            model.addAttribute("errorMessage", "Report could not be saved");
        }
        return "redirect:/adminHome";

    }

}
