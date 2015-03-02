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
import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.service.BlogService;
import cz.jiripinkas.jba.service.ItemService;

@Controller
public class BlogController {

	private static final class BlogNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@Autowired
	private ItemService itemService;

	@Autowired
	private BlogService blogService;

	@ExceptionHandler
	public void handleBlogNotFound(BlogNotFoundException exception, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	private void findBlog(String shortName, Model model) {
		Blog blog = blogService.findByShortName(shortName);
		if (blog == null) {
			throw new BlogNotFoundException();
		}
		model.addAttribute("title", "Blog: " + blog.getName());
		model.addAttribute("blogDetail", true);
		model.addAttribute("blogShortName", blog.getShortName());
		model.addAttribute("blog", blog);
	}

	@RequestMapping("/blog/{shortName}")
	public String blogDetail(@PathVariable String shortName, Model model) {
		findBlog(shortName, model);
		return showFirstPage(model, "index", shortName);
	}

	@RequestMapping(value = "/blog/{shortName}", params = "page")
	public String blogDetail(Model model, @RequestParam int page, @RequestParam String shortName) {
		findBlog(shortName, model);
		return showPage(model, page, "index", shortName);
	}

	private String showFirstPage(Model model, String tilesPage, String shortName) {
		return showPage(model, 0, tilesPage, shortName);
	}

	private String showPage(Model model, int page, String tilesPage, String shortName) {
		model.addAttribute("items", itemService.getDtoItems(page, shortName));
		model.addAttribute("nextPage", page + 1);
		return tilesPage;
	}

	@ResponseBody
	@RequestMapping(value = "/page/{page}", params = "shortName")
	public List<ItemDto> getPageLatest(@PathVariable int page, @RequestParam String shortName) {
		return itemService.getDtoItems(page, shortName);
	}

	@RequestMapping("/blogs")
	public String showBlogs(Model model) {
		model.addAttribute("blogs", blogService.findAll());
		return "blogs";
	}

}
