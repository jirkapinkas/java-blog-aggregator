package cz.jiripinkas.jba.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.service.BlogService;
import cz.jiripinkas.jba.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BlogService blogService;

	@ModelAttribute("blog")
	public Blog constructBlog() {
		return new Blog();
	}

	@RequestMapping("/blog-form")
	public String showForm(@RequestParam int blogId, Model model) {
		model.addAttribute("blog", blogService.findOne(blogId));
		return "blog-form";
	}

	@RequestMapping(value = "/blog-form", method = RequestMethod.POST)
	public String editBlog(@RequestParam int blogId, @ModelAttribute Blog blog, Model model, Principal principal, HttpServletRequest request) {
		blog.setId(blogId);
		blogService.update(blog, principal.getName(), request.isUserInRole("ROLE_ADMIN"));
		return "redirect:/account.html";
	}

	@RequestMapping("/account")
	public String account(Model model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("user", userService.findOneWithBlogs(name));
		return "account";
	}

	@RequestMapping(value = "/account", method = RequestMethod.POST)
	public String doAddBlog(Model model, @Valid @ModelAttribute("blog") Blog blog, BindingResult result, Principal principal) {
		if (result.hasErrors()) {
			return account(model, principal);
		}
		String name = principal.getName();
		blogService.save(blog, name);
		return "redirect:/account.html";
	}

	@RequestMapping(value = "/blog/remove/{id}", method = RequestMethod.POST)
	public String removeBlog(@PathVariable int id) {
		Blog blog = blogService.findOneFetchUser(id);
		blogService.delete(blog);
		return "redirect:/account.html";
	}

	@RequestMapping("/blog/available")
	@ResponseBody
	public String available(@RequestParam String url) {
		Boolean available = blogService.findOne(url) == null;
		return available.toString();
	}

}
