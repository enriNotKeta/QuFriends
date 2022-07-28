package app.controller;


import app.model.User;
import app.service.RelationshipService;
import app.service.SuggesterService;
import app.service.UserService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;
    private final RelationshipService relationshipService;
    private final SuggesterService suggesterService;


    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder encoder, RelationshipService relationshipService
                            ,SuggesterService suggesterService) {
        this.userService = userService;
        this.encoder = encoder;
        this.relationshipService = relationshipService;
        this.suggesterService = suggesterService;

    }


    @GetMapping(value = "/home")
    public String showTest(Model model) {
        List<User> users = relationshipService.getMatches(userService.getCurrentUser().getId());
        model.addAttribute("matchedUsers", users);
        model.addAttribute("suggestedUsers", suggesterService.getUsersToRecommend());
        model.addAttribute("requestingUsers",userService.getRequestingUsers());

        return "/user/user-dashboard";
    }

    // USER
    @GetMapping("/user-settings")
    public String getUserSettings(Model model) {

        User user = userService.getCurrentUser();

        model.addAttribute("name", user.getName());
        model.addAttribute("surname", user.getLastName());
        model.addAttribute("email", user.getEmail());

        return "/user/user-settings";
    }

    // Change user settings if validated
    @RequestMapping(value = "/user-settings", params = "save")
    public String editUserSettings(@RequestParam("name") String name, @RequestParam("surname") String surname,
                                   Model model) {
        userService.editUserSettings(name, surname);
        return "redirect:/user-settings";
    }

    // RESET USER SETTINGS
    @RequestMapping(value = "/user-settings", params = "reset")
    public String resetUserSettings(Model model) {

        User user = userService.getCurrentUser();
        model.addAttribute("name", user.getName());
        model.addAttribute("surname", user.getLastName());
        model.addAttribute("email", user.getEmail());

        return "/user/user-settings";
    }

    @GetMapping("/user-settings/changePassword")
    public String getUserPassword(Model model) {

        User user = userService.getCurrentUser();
        model.addAttribute("user", user);

        return "/user/changePasswordUser";
    }

    // SAVE USER PASSWORD
    @PostMapping("user-changePassword")
    public ModelAndView saveUserPassword(User user, Model model, @RequestParam("oldPassword") String oldPassword,
                                         @RequestParam("newPassword") String newPassword,
                                         @RequestParam("confirmNewPassword") String confirmNewPassword) {
        user = userService.getCurrentUser();
        if (user != null) {
            if (!encoder.matches(oldPassword, user.getPassword())) {
                return new ModelAndView("user/changePasswordUser",
                        ImmutableMap.of("message", "Old password is not correct!"));
            }
            if (newPassword == null || newPassword == "") {
                return new ModelAndView("user/changePasswordUser",
                        ImmutableMap.of("message", "Please enter new password"));
            }
            if (!newPassword.equalsIgnoreCase(confirmNewPassword)) {
                return new ModelAndView("user/changePasswordUser",
                        ImmutableMap.of("message", "Passwords do not match"));
            }
            if (newPassword.equalsIgnoreCase(confirmNewPassword)) {
                userService.savePassword(user, confirmNewPassword);
                return new ModelAndView("user/changePasswordUser",
                        ImmutableMap.of("message", "Password is changed successfully!"));
            }

        } else {
            return new ModelAndView("user/changePasswordUser", ImmutableMap.of("message", "User do not match"));
        }

        return new ModelAndView("redirect:/user-settings/changePassword");
    }
}