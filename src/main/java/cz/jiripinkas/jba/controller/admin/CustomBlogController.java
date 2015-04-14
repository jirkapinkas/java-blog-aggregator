package cz.jiripinkas.jba.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.jiripinkas.jba.entity.CustomBlog;
import cz.jiripinkas.jba.service.CustomBlogService;

@Controller
public class CustomBlogController {

	@Autowired
	private CustomBlogService customBlogService;

	@ModelAttribute
	public CustomBlog construct() {
		return new CustomBlog();
	}

	@RequestMapping("/custom-blog")
	public String showBlogs(Model model, @RequestParam(defaultValue = "0") int page) {
		model.addAttribute("blogs", customBlogService.findBlogs(page));
		model.addAttribute("currPage", page);
		return "custom-blog-list";
	}

	@RequestMapping("/custom-blog/{shortName}")
	public String showDetail(Model model, @PathVariable String shortName) {
		model.addAttribute("blog", customBlogService.findOne(shortName));
		return "custom-blog-detail";
	}

	@RequestMapping("/custom-blog/add")
	public String showAdd() {
		return "custom-blog-form";
	}

	@RequestMapping("/custom-blog/edit/{shortName}")
	public String showEdit(Model model, @PathVariable String shortName) {
		model.addAttribute("customBlog", customBlogService.findOne(shortName));
		return "custom-blog-form";
	}

	@RequestMapping(value = "/custom-blog/edit/{shortName}", method = RequestMethod.POST)
	public String edit(@ModelAttribute CustomBlog customBlog, RedirectAttributes redirectAttributes) {
		customBlogService.save(customBlog);
		redirectAttributes.addFlashAttribute("success", true);
		return "redirect:/custom-blog/edit/" + customBlog.getShortName() + ".html";
	}

	@RequestMapping(value = "/custom-blog/add", method = RequestMethod.POST)
	public String insert(@ModelAttribute CustomBlog customBlog, RedirectAttributes redirectAttributes) {
		customBlogService.save(customBlog);
		redirectAttributes.addFlashAttribute("success", true);
		return "redirect:/custom-blog/add.html";
	}
}
