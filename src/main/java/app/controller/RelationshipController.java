package app.controller;

import app.model.User;
import app.service.RelationshipService;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class RelationshipController {
    private final UserService userService;
    private final RelationshipService relationshipService;

    @Autowired
    public RelationshipController(RelationshipService relationshipService, UserService userService) {
        this.relationshipService = relationshipService;
        this.userService = userService;
    }

    @GetMapping(value = "/matches")
    public String showMatches(Model model) {
        System.out.println(userService.getCurrentUser()+ ", currentUser");
        List<User> users = relationshipService.getMatches(userService.getCurrentUser().getId());
        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("users", users);
        System.out.println("usersMatqq " + users.size());

        return "/user/matches";
    }

    @PostMapping(value = "/umatch/user/{userId}")
    public String umatchUsers(Model model, @PathVariable("userId") Long userId) {
        System.out.println("usera: " + userId);
        relationshipService.umatchUsers(userId);
        return "redirect:/matches";
    }

    @PostMapping(value = "/block/user/{userId}")
    public String blockUser(Model model, @PathVariable("userId") Long userId) {
        System.out.println("usera: " + userId);
        relationshipService.blockUser(userId);
        return "redirect:/matches";
    }


}
