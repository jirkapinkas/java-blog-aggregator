package cz.jiripinkas.jba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.jiripinkas.jba.dto.CategoryDto;
import cz.jiripinkas.jba.entity.Category;
import cz.jiripinkas.jba.service.CategoryService;

@Controller
public class AdminCategoryController {

	@Autowired
	private CategoryService categoryService;

	@RequestMapping("/admin-categories")
	public String categories(Model model) {
		model.addAttribute("categories", categoryService.findAll());
		return "admin-categories";
	}

	@ModelAttribute
	public Category construct() {
		return new Category();
	}

	@RequestMapping(value = "/admin-categories", method = RequestMethod.POST)
	public String save(@ModelAttribute Category category) {
		categoryService.save(category);
		return "redirect:/admin-categories.html";
	}

	@RequestMapping(value = "/admin-categories/delete/{id}", method = RequestMethod.POST)
	public String delete(@PathVariable int id) {
		categoryService.delete(id);
		return "redirect:/admin-categories.html";
	}

	@ResponseBody
	@RequestMapping("/admin-categories/{id}")
	public CategoryDto categoryShortName(@PathVariable int id) {
		return categoryService.findOneDto(id);
	}

	@ResponseBody
	@RequestMapping(value = "/admin-categories/set/{blogId}/cat/{categoryId}", method = RequestMethod.POST)
	public String setMapping(@PathVariable int blogId, @PathVariable int categoryId) {
		categoryService.addMapping(blogId, categoryId);
		return "ok";
	}

}
