package cz.jiripinkas.jba.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.jiripinkas.jba.dto.ItemDto;
import cz.jiripinkas.jba.entity.Category;
import cz.jiripinkas.jba.service.BlogService;
import cz.jiripinkas.jba.service.CategoryService;
import cz.jiripinkas.jba.service.ItemService;

@Controller
public class CategoryController {

	private static final class CategoryNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private ItemService itemService;

	@ExceptionHandler
	public void handleCategoryNotFound(CategoryNotFoundException exception, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	private void findCategory(String shortName, Model model) {
		Category category = categoryService.findByShortName(shortName);
		if (category == null) {
			throw new CategoryNotFoundException();
		}
		model.addAttribute("title", "Category: " + category.getName());
		model.addAttribute("current", "category");
		model.addAttribute("categoryDetail", true);
		model.addAttribute("categoryShortName", category.getShortName());
	}

	private String showFirstPage(Model model, String tilesPage, String shortName) {
		return showPage(model, 0, tilesPage, shortName);
	}

	private String showPage(Model model, int page, String tilesPage, String shortName) {
		model.addAttribute("items", itemService.getCategoryDtoItems(page, shortName));
		model.addAttribute("nextPage", page + 1);
		return tilesPage;
	}

	@RequestMapping("/category/{shortName}")
	public String categoryDetail(@PathVariable String shortName, Model model) {
		findCategory(shortName, model);
		return showFirstPage(model, "category", shortName);
	}

	@RequestMapping(value = "/category/{categoryShortName}", params = "page")
	public String categoryDetail(Model model, @RequestParam int page, @PathVariable String categoryShortName) {
		findCategory(categoryShortName, model);
		return showPage(model, page, "category", categoryShortName);
	}

	@ResponseBody
	@RequestMapping(value = "/page/{page}", params = "categoryShortName")
	public List<ItemDto> getPageLatest(@PathVariable int page, @RequestParam String categoryShortName) {
		return itemService.getCategoryDtoItems(page, categoryShortName);
	}

}
