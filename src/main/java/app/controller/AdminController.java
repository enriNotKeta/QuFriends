package app.controller;


import app.model.User;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

	private final UserService userService;

	@Autowired
	public AdminController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/adminHome")
	public ModelAndView adminhome(ModelAndView modelAndView) {
		modelAndView = new ModelAndView("/admin/adminHome");
		return modelAndView;
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

}
