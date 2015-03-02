package cz.jiripinkas.jba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.jiripinkas.jba.entity.Configuration;
import cz.jiripinkas.jba.service.ConfigurationService;

@Controller
public class AdminConfigurationController {

	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping("/configuration")
	public String show(Model model) {
		model.addAttribute("configuration", configurationService.find());
		return "configuration";
	}

	@RequestMapping(value = "/configuration", method = RequestMethod.POST)
	public String save(@ModelAttribute Configuration configuration, RedirectAttributes redirectAttributes) {
		configurationService.save(configuration);
		redirectAttributes.addFlashAttribute("success", true);
		return "redirect:/configuration.html";
	}
}
