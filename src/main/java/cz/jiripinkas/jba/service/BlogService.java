package cz.jiripinkas.jba.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.entity.Item;
import cz.jiripinkas.jba.entity.User;
import cz.jiripinkas.jba.exception.RssException;
import cz.jiripinkas.jba.repository.BlogRepository;
import cz.jiripinkas.jba.repository.ItemRepository;
import cz.jiripinkas.jba.repository.UserRepository;

@Service
public class BlogService {

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RssService rssService;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemService itemService;

	private Date lastIndexedDateFinish;

	public void saveItems(Blog blog) {
		try {
			List<Item> items = rssService.getItems(blog.getUrl());
			for (Item item : items) {
				if (!itemService.isTooOld(item.getPublishedDate())) {
					Item savedItem = itemRepository.findByBlogAndLink(blog, item.getLink());
					if (savedItem == null) {
						item.setBlog(blog);
						itemRepository.save(item);
					}
				}
			}
		} catch (RssException e) {
			System.out.println("exception during downloading: " + blog.getUrl());
			System.out.println("message: " + e.getMessage());
		}
	}

	// 0.5 hour = 60 seconds * 30 minutes * 1000
	@Scheduled(fixedDelay = 1800000)
	public void reloadBlogs() {
		List<Blog> blogs = blogRepository.findAll();
		for (Blog blog : blogs) {
			saveItems(blog);
		}
		lastIndexedDateFinish = new Date();
	}

	public int getLastIndexDateMinutes() {
		return (int) ((new Date().getTime() - lastIndexedDateFinish.getTime()) / (1000 * 60));
	}

	public void save(Blog blog, String name) {
		User user = userRepository.findByName(name);
		blog.setUser(user);
		blogRepository.save(blog);
		saveItems(blog);
	}

	@PreAuthorize("#blog.user.name == authentication.name or hasRole('ROLE_ADMIN')")
	public void delete(@P("blog") Blog blog) {
		blogRepository.delete(blog);
	}

	public Blog findOne(int id) {
		return blogRepository.findOne(id);
	}

	public Blog findOne(String url) {
		return blogRepository.findByUrl(url);
	}

	public Blog findOneFetchUser(int id) {
		return blogRepository.findOneFetchUser(id);
	}

	public long count() {
		return blogRepository.count();
	}

}