package app.controller;

import app.model.Hobby;
import app.model.UserReport;
import app.repository.UserRepository;
import app.service.HobbyService;
import com.google.common.collect.ImmutableMap;
import app.model.User;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
public class AuthenticationController {

    private final UserService userService;
    private final HobbyService hobbyService;
    @Autowired
    public AuthenticationController(UserService userService, HobbyService hobbyService) {
        this.userService = userService;
        this.hobbyService = hobbyService;
    }

    // LOGIN
    @RequestMapping({"/","/login"})
    public ModelAndView login(User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/auth/login");
        return modelAndView;
    }

    // LOGOUT
    @RequestMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerPage() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("/auth/register");
        return modelAndView;
    }

    // SAVE USER
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUser(@Valid User user, @RequestParam("image") MultipartFile multipartFile,
                                     BindingResult bindingResult, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("successMessage", "Please correct the errors in form!");
            modelMap.addAttribute("bindingResult", bindingResult);
        } else if (userService.isUserAlreadyPresent(user)) {
            modelAndView.addObject("successMessage", "User is already registered!");
        }
        else {
            User regUser = userService.registerUser(user, multipartFile);
            List<Hobby> hobbies = hobbyService.findAll();
            System.out.println(regUser + ", reguserr");
            System.out.println(hobbies + ", hobbies");
            modelAndView.addObject("registeredUser", regUser);
            modelAndView.addObject("hobbies", hobbies);
            modelAndView.setViewName("/user/test");
            return modelAndView;


        }
        modelAndView.addObject("user", new User());
        modelAndView.setViewName("/auth/register");
        return modelAndView;
    }


    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public ModelAndView ResetPasswordPage(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("/auth/forgotPassword");
        return modelAndView;
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
    public ModelAndView forgotUserPassword(@RequestParam("email") String email, ModelAndView modelAndView) {
        User existingUser = userService.findByEmail(email);
        if (existingUser != null) {
//            userService.sendEmail(existingUser);
            modelAndView.addObject("token", existingUser.getResetToken());
            return new ModelAndView("/auth/forgotPassword", ImmutableMap.of("message", "Check your email!"));
        } else {
            return new ModelAndView("/auth/forgotPassword", ImmutableMap.of("message", "Email is not valid!"));
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ModelAndView displayResetPasswordPage(ModelAndView modelAndView, @RequestParam("token") String token) {
        User user = userService.findUserByResetToken(token);
        if (user != null) {
            modelAndView.setViewName("/auth/resetPassword");
        }
        return modelAndView;
    }

    @PostMapping("/reset")
    public ModelAndView savePassword(User user, @RequestParam("password") String password,
                                     @RequestParam("passwordConfirmation") String passwordConfirmation, @RequestParam("token") String token) {

        user = userService.findUserByResetToken(token);
        if (user != null) {

            if (token == null || token == "") {
                return new ModelAndView("/auth/resetPassword", ImmutableMap.of("message", "Please enter token"));
            }
            if (user != null)
                if (password == null || password == "") {
                    return new ModelAndView("/auth/resetPassword", ImmutableMap.of("message", "Please enter password"));
                }
            if (!password.equalsIgnoreCase(passwordConfirmation)) {
                return new ModelAndView("/auth/resetPassword", ImmutableMap.of("message", "Passwords do not match"));
            }
            userService.savePassword(user, password);
        } else {
            return new ModelAndView("/auth/resetPassword", ImmutableMap.of("message", "Token do not match"));
        }

        return new ModelAndView("/auth/resetPassword", ImmutableMap.of("message", "Password changed successfully!"));
    }

    @GetMapping(path = "/delete/{id}")
    public String deleteUserById(Model model,
                                 @PathVariable("id") Long id) {

        userService.deleteUserById(id);
        return "redirect:/adminHome";
    }
}
