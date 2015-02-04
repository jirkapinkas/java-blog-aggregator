package cz.jiripinkas.jba.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.jiripinkas.jba.entity.User;
import cz.jiripinkas.jba.service.UserService;

@Controller
public class AdminDetailController {

	@Autowired
	private UserService userService;

	@RequestMapping("/admin-detail")
	public void show(Model model) {
		model.addAttribute("user", userService.findAdmin());
	}

	@RequestMapping(value = "/admin-detail", method = RequestMethod.POST)
	public String save(@ModelAttribute @Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return null;
		}
		userService.saveAdmin(user);
		redirectAttributes.addFlashAttribute("success", true);
		return "redirect:/admin-detail.html";
	}
}
