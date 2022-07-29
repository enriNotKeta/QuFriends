package app.controller;


import app.model.StatisticsPDFExporter;
import app.model.User;
import app.model.UserHobby;
import app.model.UserReport;
import app.service.UserReportService;
import app.service.UserService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

	private final UserService userService;
	private final UserReportService userReportService;

	@Autowired
	public AdminController(UserService userService, UserReportService userReportService) {
		this.userService = userService;
		this.userReportService = userReportService;
	}

	@GetMapping(value = "/adminHome")
	public String showTest(Model model) {
		List<UserReport> userReports = userReportService.getReports();
		System.out.println(userReports + ", usereportzz");
		model.addAttribute("userReports", userReports);
		model.addAttribute("currentUser", userService.getCurrentUser());


		return "/admin/adminHome";
	}

	@GetMapping("/addAdmin")
	public String addAdminPage(Model model) {
		List<User> user = userService.findAll();
		model.addAttribute("user", user);
		return "/admin/addAdmin";
	}

	@RequestMapping(value = "/controlUser", params = "adminId")
	public String createAdmin(User user, @RequestParam("adminId") Integer adminId) {
		userService.setAdmin(user, adminId);
		return "redirect:/addAdmin";
	}

	@RequestMapping(value = "/controlUser", params = "userId")
	public String createUser(User user, @RequestParam("userId") Integer userId) {
		userService.setUser(user, userId);
		return "redirect:/addAdmin";
	}
	@GetMapping(path = { "/edit", "/edit/{id}" })
	public String editUserById(Model model,
							   @PathVariable("id") Optional<Long> id) {
		if (id.isPresent()) {
			User entity = userService.getUserById(id.get());
			model.addAttribute("user", entity);
		} else {
			model.addAttribute("user", new User());
		}
		return "admin/edit-user";
	}
	@PostMapping(path = "/updateUser")
	public String updateUser(User user) {
		userService.updateUser(user);
		return "redirect:/addAdmin";
	}

	@PostMapping(value = "/ban/user/{userId}")
	public String banUser(Model model, @PathVariable("userId") Long userId) {
		userService.banUser(userId);
		return "redirect:/adminHome";
	}

	@PostMapping(value = "/ignore/report/{userReportId}")
	public String ignoreUserReport(Model model, @PathVariable("userReportId") Long userReportId) {
		userReportService.ignoreUserReport(userReportId);
		return "redirect:/adminHome";
	}


}
