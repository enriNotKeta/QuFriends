package app.controller;

import app.service.RecommendationService;
import app.service.UserService;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserService userService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService,UserService userService) {
        this.recommendationService = recommendationService;
        this.userService = userService;
    }

    @GetMapping(value = "/test")
    public String showTest(Model model) {
        Recommender recommender = null;
        try {
            recommender = recommendationService.getRecommender();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<RecommendedItem> recommendedItems = null;
        try {
            recommendedItems = recommendationService.getRecommendations(
                                                recommender,4L, 5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        recommendationService.displayRecommendations(3, recommendedItems);

        model.addAttribute("user", userService.getCurrentUser());
        return "user/user-dashboard";
    }

}
