package com.ven.controllers;

import com.ven.model.user.SignupForm;
import com.ven.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "contact")
public class ContactController {

	@Autowired
	private UserService userService;

	@ModelAttribute("module")
    String module() {
		return "contact";
	}

	/**
	 * 注册页面加载
	 * @param model
	 * @return
	 */
	@GetMapping(value = { "", "/index" })
	public String index(Model model) {
		model.addAttribute("signupForm", new SignupForm());
		return "contact/index";
	}

	/**
	 * 注册页面提交
	 * @param signupForm
	 * @param errors
	 * @param ra
	 * @return
	 */
	@PostMapping("/signup")
	public String signup(@Valid @ModelAttribute SignupForm signupForm, Errors errors, RedirectAttributes ra) {
		if (errors.hasErrors()) {
			return "contact/signup";
		}
		userService.save(signupForm.createUser());
		return "home/index";
	}
	
}
