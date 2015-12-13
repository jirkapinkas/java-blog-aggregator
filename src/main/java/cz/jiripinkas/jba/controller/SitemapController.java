package cz.jiripinkas.jba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.entity.Configuration;
import cz.jiripinkas.jba.entity.NewsItem;
import cz.jiripinkas.jba.service.BlogService;
import cz.jiripinkas.jba.service.ConfigurationService;
import cz.jiripinkas.jba.service.NewsService;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.WebSitemapGenerator;

@Controller
public class SitemapController {
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private NewsService newsService;
	
	@ResponseBody
	@RequestMapping("/robots.txt")
	public String getRobots() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Sitemap: ");
		stringBuilder.append(configurationService.find().getChannelLink());
		stringBuilder.append("/sitemap.xml");
		stringBuilder.append("\n");
		stringBuilder.append("User-agent: *");
		stringBuilder.append("\n");
		stringBuilder.append("Allow: /");
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}

	@ResponseBody
	@RequestMapping("/sitemap")
	public String getSitemap() {
		Configuration configuration = configurationService.find();
		WebSitemapGenerator webSitemapGenerator = new WebSitemapGenerator(configuration.getChannelLink());
		webSitemapGenerator.addPage(new WebPage().setName(""));
		webSitemapGenerator.addPage(new WebPage().setName("blogs.html"));
		webSitemapGenerator.addPage(new WebPage().setName("index.html?top-views&amp;max=week"));
		webSitemapGenerator.addPage(new WebPage().setName("index.html?top-views&amp;max=month"));
		webSitemapGenerator.addPage(new WebPage().setName("news.html"));
		for (Blog blog : blogService.findAll()) {
			webSitemapGenerator.addPage(new WebPage().setName("blog/" + blog.getShortName() + ".html"));
		}
		for (NewsItem newsItem : newsService.findAll()) {
			webSitemapGenerator.addPage(new WebPage().setName("news/" + newsItem.getShortName() + ".html"));
		}
		return webSitemapGenerator.constructSitemapString();
	}

}
