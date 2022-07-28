package app.controller;

import app.model.Hobby;
import app.model.Report;
import app.model.User;
import app.service.*;
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
    private final HobbyService hobbyService;


    @Autowired
    public SuggesterController(SuggesterService suggesterService, UserService userService,  HobbyService hobbyService) {
        this.suggesterService = suggesterService;
        this.userService = userService;
        this.hobbyService = hobbyService;

    }

    @GetMapping(value = "/suggested")
    public String showSuggestedUsers(Model model) {
        model.addAttribute("users", suggesterService.getUsersToRecommend());
        model.addAttribute("currentUser", userService.getCurrentUser());

        return "user/suggested";
    }

    @GetMapping(value = "/hobbies")
    public String showHobbies(Model model) {
        List<Hobby> hobbies = hobbyService.findAll();
        model.addAttribute("hobbies", hobbies);
        return "/user/hobbies";
    }




}
