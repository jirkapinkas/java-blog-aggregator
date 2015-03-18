package cz.jiripinkas.jba.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.jiripinkas.jba.entity.Category;

@Service
public class AllCategoriesService {

	@Autowired
	private CategoryService categoryService;

	public Integer[] getAllCategoryIds() {
		List<Category> categories = categoryService.findAll();
		Integer[] result = new Integer[categories.size()];
		for (int i = 0; i < categories.size(); i++) {
			result[i] = categories.get(i).getId();
		}
		return result;
	}

}
