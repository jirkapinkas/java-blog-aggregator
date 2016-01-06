package cz.jiripinkas.jba.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.jiripinkas.jba.entity.Configuration;
import cz.jiripinkas.jba.service.ConfigurationService;

@Controller
public class AdminConfigurationController {

	private static final Logger log = LoggerFactory.getLogger(AdminUsersController.class);

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

	@RequestMapping(value = "configuration/upload-icon", method = RequestMethod.POST)
	public String uploadIcon(@RequestParam MultipartFile icon, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("success", true);
		if (!icon.isEmpty()) {
			try {
				log.info("save icon");
				configurationService.saveIcon(icon.getBytes());
			} catch (Exception e) {
				log.error("could not upload icon", e);
			}
		} else {
			log.error("could not upload icon");
		}
		return "redirect:/configuration.html";
	}

	@RequestMapping(value = "configuration/upload-favicon", method = RequestMethod.POST)
	public String uploadFavicon(@RequestParam MultipartFile favicon, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("success", true);
		if (!favicon.isEmpty()) {
			try {
				log.info("save favicon");
				configurationService.saveFavicon(favicon.getBytes());
			} catch (Exception e) {
				log.error("could not upload favicon", e);
			}
		} else {
			log.error("could not upload favicon");
		}
		return "redirect:/configuration.html";
	}

	@RequestMapping(value = "configuration/upload-appleTouchIcon", method = RequestMethod.POST)
	public String uploadAppleTouchIcon(@RequestParam MultipartFile appleTouchIcon, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("success", true);
		if (!appleTouchIcon.isEmpty()) {
			try {
				log.info("save apple touch icon");
				configurationService.saveAppleTouchIcon(appleTouchIcon.getBytes());
			} catch (Exception e) {
				log.error("could not upload appleTouchIcon", e);
			}
		} else {
			log.error("could not upload appleTouchIcon");
		}
		return "redirect:/configuration.html";
	}

}
