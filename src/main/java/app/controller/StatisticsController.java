package app.controller;

import app.model.Hobby;
import app.model.StatisticsPDFExporter;
import app.model.UserHobby;
import app.model.UserReport;
import app.service.UserHobbyService;
import app.service.UserReportService;
import app.service.UserService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class StatisticsController {

    private final UserService userService;
    private final UserHobbyService userHobbyService;
    private final UserReportService userReportService;

    @Autowired
    public StatisticsController(UserService userService, UserReportService userReportService, UserHobbyService userHobbyService) {
        this.userService = userService;
        this.userReportService = userReportService;
        this.userHobbyService = userHobbyService;
    }


    @GetMapping("/api/statistics/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=statistics_info_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        Map<Hobby,Double> mapHobbyAvfRatings = userHobbyService.getAvgRatingPerHobby();

        StatisticsPDFExporter exporter = new StatisticsPDFExporter(mapHobbyAvfRatings);
        exporter.export(response);

    }
}
