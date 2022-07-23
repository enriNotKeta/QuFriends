package app.controller;


import app.model.User;
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

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping(value = "/home")
    public ModelAndView home() {
        // Assign the data fetched with the GUI components
        ModelAndView modelAndView = new ModelAndView("/user/user-dashboard");
        return modelAndView;
    }

    // USER
    @GetMapping("/user-settings")
    public String getUserSettings(Model model) {

        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(auth);

        model.addAttribute("name", user.getName());
        model.addAttribute("surname", user.getLastName());
        model.addAttribute("jobPosition", user.getJobPosition());
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

        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(auth);
        model.addAttribute("name", user.getName());
        model.addAttribute("surname", user.getLastName());
        model.addAttribute("jobPosition", user.getJobPosition());
        model.addAttribute("email", user.getEmail());

        return "/user/user-settings";
    }

    @GetMapping("/user-settings/changePassword")
    public String getUserPassword(Model model) {

        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(auth);
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