package cz.jiripinkas.jba.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cz.jiripinkas.jba.service.BlogService;
import cz.jiripinkas.jba.service.UserService;

@Controller
@RequestMapping("/users")
public class AdminUsersController {

	private static final Logger log = LoggerFactory.getLogger(AdminUsersController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private BlogService blogService;

	@RequestMapping
	public String users(Model model) {
		model.addAttribute("users", userService.findAll());
		return "users";
	}

	@RequestMapping("/{id}")
	public String detail(Model model, @PathVariable int id) {
		model.addAttribute("user", userService.findOneWithBlogs(id));
		return "user-detail";
	}

	@RequestMapping("/remove/{id}")
	public String removeUser(@PathVariable int id) {
		userService.delete(id);
		return "redirect:/users.html";
	}

	@RequestMapping(value = "/{id}", method=RequestMethod.POST)
	public String uploadIcon(@RequestParam MultipartFile icon, @RequestHeader String referer, @RequestParam int blogId, @PathVariable("id") int userId) {
		if (!icon.isEmpty()) {
			try {
				System.out.println("save blog " + blogId);
				blogService.saveIcon(blogId, icon.getBytes());
			} catch (Exception e) {
				log.error("could not upload icon", e);
			}
		} else {
			log.error("could not upload icon");
		}
		return "redirect:" + referer;
	}

}
