package cz.jiripinkas.jba.service;

import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cz.jiripinkas.jba.dto.CategoryDto;
import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.entity.Category;
import cz.jiripinkas.jba.repository.BlogRepository;
import cz.jiripinkas.jba.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private Mapper mapper;

	@Cacheable("categories")
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@CacheEvict(value = "categories", allEntries = true)
	public void save(Category category) {
		categoryRepository.save(category);
	}

	@CacheEvict(value = "categories", allEntries = true)
	public void delete(int id) {
		categoryRepository.delete(id);
	}

	public CategoryDto findOneDto(int id) {
		return mapper.map(categoryRepository.findOne(id), CategoryDto.class);
	}
	
	public Category findByShortName(String shortName) {
		return categoryRepository.findByShortName(shortName);
	}

	public void addMapping(int blogId, int categoryId) {
		Category category = categoryRepository.findOne(categoryId);
		Blog blog = blogRepository.findOne(blogId);
		blog.setCategory(category);
		blogRepository.save(blog);
	}

}
