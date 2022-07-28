package app.controller;

import app.model.Hobby;
import app.model.Report;
import app.model.User;
import app.model.UserReport;
import app.repository.ReportRepository;
import app.repository.UserReportRepository;
import app.service.*;
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
    private final HobbyService hobbyService;


    @Autowired
    public ReportController(ReportService reportService, UserService userService,
                            UserReportService userReportService, HobbyService hobbyService) {
        this.reportService = reportService;
        this.userService = userService;
        this.userReportService = userReportService;
        this.hobbyService = hobbyService;
    }


    @GetMapping(value = "/report/user/{userId}")
    public String showReport(Model model, @PathVariable long userId) {
        List<Report> reports = reportService.findAll();
        UserReport userReport = new UserReport();
        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("reports", reports);
        model.addAttribute("userreport", userReport);
        model.addAttribute("userId", userId);


        return "/user/report-send";
    }

    @PostMapping(value = "/report/user/{userId}")
    public String showReport(Model model, @ModelAttribute("userreport") UserReport userreport, @PathVariable long userId) {
        //fixed coz issues w thymeleaf
        Report report  = reportService.findById(1L);
        userreport.setReport(report);

        try {
            UserReport newUserReport = userReportService.save(userreport, userId);
            return "redirect:/home";

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
