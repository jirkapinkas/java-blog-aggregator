package cz.jiripinkas.jba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.jiripinkas.jba.service.AllCategoriesService;

@Controller
public class CategoryController {

	@Autowired
	private AllCategoriesService allCategoriesService;

	@RequestMapping("/all-categories")
	@ResponseBody
	public Integer[] getCategories() {
		return allCategoriesService.getAllCategoryIds();
	}

}
