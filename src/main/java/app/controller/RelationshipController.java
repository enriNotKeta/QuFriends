package app.controller;

import app.exception.BadResourceException;
import app.exception.ResourceAlreadyExistsException;
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
        List<User> users = relationshipService.getMatches(userService.getCurrentUser().getId());
        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("users", users);

        return "/user/matches";
    }


    @PostMapping(value = "/umatch/user/{userId}")
    public String umatchUsers(Model model, @PathVariable("userId") Long userId) {
        relationshipService.umatchUsers(userId);
        return "redirect:/matches";
    }

    @PostMapping(value = "/block/user/{userId}/{location}")
    public String blockUser(Model model, @PathVariable("userId") Long userId, @PathVariable("location") String location) {
        relationshipService.blockUser(userId);

        if (location.equals("suggested")) {
            return "redirect:/suggested";
        } else if (location.equals("matches")) {
            return "redirect:/matches";
        }else if (location.equals("requests")) {
            return "redirect:/requests";
        }

        return "redirect:/home";
    }

    @PostMapping(value = "/request/user/{userId}")
    public String requestUser(Model model, @PathVariable("userId") Long userId) {
        try {
            relationshipService.requestUser(userId);
        } catch (ResourceAlreadyExistsException e) {
            String errorMessage = e.getMessage();
            model.addAttribute("errorMessage", errorMessage);
        }
        return "redirect:/suggested";
    }


    @GetMapping(value = "/requests")
    public String showRequests(Model model) {
        List<User> users = userService.getRequestingUsers();
        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("users", users);

        return "/user/requests";
    }


    @PostMapping(value = "/request/accept/user/{userId}")
    public String blockUser(Model model, @PathVariable("userId") Long userId) {
        try {
            relationshipService.acceptRequest(userId);
        } catch (BadResourceException e) {
            String errorMessage = e.getMessage();
            model.addAttribute("errorMessage", errorMessage);
        }

        return "redirect:/requests";
    }

}
