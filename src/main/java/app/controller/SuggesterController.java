package app.controller;

import app.model.Report;
import app.model.User;
import app.service.ReportService;
import app.service.SuggesterService;
import app.service.UserReportService;
import app.service.UserService;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SuggesterController {
    private final UserService userService;
    private final SuggesterService suggesterService;

    @Autowired
    public SuggesterController(SuggesterService suggesterService, UserService userService) {
        this.suggesterService = suggesterService;
        this.userService = userService;
    }

    @GetMapping(value = "/suggested")
    public String showSuggestedUsers(Model model) {
        model.addAttribute("users", suggesterService.getUsersToRecommend());
        model.addAttribute("currentUser", userService.getCurrentUser());

        return "user/suggested";
    }




}
