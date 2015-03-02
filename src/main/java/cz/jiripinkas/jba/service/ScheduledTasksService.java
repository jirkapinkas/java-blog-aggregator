package cz.jiripinkas.jba.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.entity.Item;
import cz.jiripinkas.jba.repository.BlogRepository;
import cz.jiripinkas.jba.repository.ItemRepository;

@Service
public class ScheduledTasksService {

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private BlogService blogService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemRepository itemRepository;

	/**
	 * For each blog retrieve latest items and store them into database.
	 */
	// 1 hour = 60 seconds * 60 minutes * 1000
	@Scheduled(fixedDelay = 3600000)
	@CacheEvict(value = "itemCount", allEntries = true)
	public void reloadBlogs() {
		List<Blog> blogs = blogRepository.findAll();
		for (Blog blog : blogs) {
			blogService.saveItems(blog);
		}
		blogService.setLastIndexedDateFinish(new Date());
	}

	/**
	 * Remove too old items without any clicks ... nobody will see them anyway.
	 */
	// 86400000 = one day = 60 * 60 * 24 * 1000
	@Scheduled(initialDelay = 86400000, fixedDelay = 86400000)
	@CacheEvict(value = "itemCount", allEntries = true)
	public void cleanOldItems() {
		List<Item> items = itemRepository.findAll();
		for (Item item : items) {
			if (item.getClickCount() == 0 && itemService.isTooOld(item.getPublishedDate())) {
				itemRepository.delete(item);
			}
		}
	}

}
