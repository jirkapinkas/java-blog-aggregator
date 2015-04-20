package cz.jiripinkas.jba.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.jiripinkas.jba.entity.NewsItem;
import cz.jiripinkas.jba.rss.TRss;
import cz.jiripinkas.jba.service.NewsService;

@Controller
public class NewsController {

	@Autowired
	private NewsService newsService;

	@ModelAttribute
	public NewsItem construct() {
		return new NewsItem();
	}
	
	@ResponseBody
	@RequestMapping("/news/feed")
	public TRss rss() {
		return newsService.getFeed();
	}

	@RequestMapping("/news")
	public String showBlogs(Model model, @RequestParam(defaultValue = "0") int page) {
		model.addAttribute("newsPage", newsService.findBlogs(page));
		model.addAttribute("currPage", page);
		return "news-list";
	}

	@RequestMapping("/news/{shortName}")
	public String showDetail(Model model, @PathVariable String shortName) {
		model.addAttribute("news", newsService.findOne(shortName));
		return "news-detail";
	}

	@RequestMapping("/admin-news/add")
	public String showAdd() {
		return "news-form";
	}

	@RequestMapping(value = "/admin-news/add", method = RequestMethod.POST)
	public String insert(@ModelAttribute NewsItem newsItem, RedirectAttributes redirectAttributes) {
		newsService.save(newsItem);
		redirectAttributes.addFlashAttribute("success", true);
		return "redirect:/admin-news/add.html";
	}

	@RequestMapping("/admin-news/edit/{shortName}")
	public String showEdit(Model model, @PathVariable String shortName) {
		model.addAttribute("newsItem", newsService.findOne(shortName));
		return "news-form";
	}

	@RequestMapping(value = "/admin-news/edit/{shortName}", method = RequestMethod.POST)
	public String edit(@ModelAttribute NewsItem newsItem, RedirectAttributes redirectAttributes) {
		newsService.save(newsItem);
		redirectAttributes.addFlashAttribute("success", true);
		return "redirect:/admin-news/edit/" + newsItem.getShortName() + ".html";
	}

	@RequestMapping(value = "/admin-news/delete/{id}", method = RequestMethod.POST)
	public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
		newsService.delete(id);
		redirectAttributes.addFlashAttribute("success", true);
		return "redirect:/news.html";
	}

}
