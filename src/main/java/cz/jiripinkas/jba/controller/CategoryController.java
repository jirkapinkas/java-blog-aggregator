package cz.jiripinkas.jba.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@RequestMapping("/all-categories")
	@ResponseBody
	public int[] getCategories() {
		List<Category> categories = categoryService.findAll();
		int[] result = new int[categories.size()];
		for (int i = 0; i < categories.size(); i++) {
			result[i] = categories.get(i).getId();
		}
		return result;
	}

}
