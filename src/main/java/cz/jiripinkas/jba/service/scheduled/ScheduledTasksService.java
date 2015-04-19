package cz.jiripinkas.jba.service.scheduled;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cz.jiripinkas.jba.dto.ItemDto;
import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.entity.Category;
import cz.jiripinkas.jba.entity.Configuration;
import cz.jiripinkas.jba.entity.Item;
import cz.jiripinkas.jba.entity.NewsItem;
import cz.jiripinkas.jba.repository.BlogRepository;
import cz.jiripinkas.jba.repository.ItemRepository;
import cz.jiripinkas.jba.repository.NewsItemRepository;
import cz.jiripinkas.jba.service.BlogService;
import cz.jiripinkas.jba.service.CategoryService;
import cz.jiripinkas.jba.service.ConfigurationService;
import cz.jiripinkas.jba.service.ItemService;
import cz.jiripinkas.jba.service.ItemService.MaxType;
import cz.jiripinkas.jba.service.ItemService.OrderType;
import cz.jiripinkas.jba.service.NewsService;

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

	@Autowired
	private NewsItemRepository newsItemRepository;

	@Autowired
	private NewsService newsService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CategoryService categoryService;

	/**
	 * For each blog retrieve latest items and store them into database.
	 */
	// 1 hour = 60 seconds * 60 minutes * 1000
	@Scheduled(fixedDelay = 60 * 60 * 1000)
	@CacheEvict(value = "itemCount", allEntries = true)
	public void reloadBlogs() {
		// first process blogs which have aggregator = null,
		// next blogs with aggregator = false
		// and last blogs with aggregator = true
		List<Blog> blogs = blogRepository.findAll(new Sort(Direction.ASC, "aggregator"));
		List<String> allLinks = itemRepository.findAllLinks();
		List<String> allLowercaseTitles = itemRepository.findAllLowercaseTitles();
		Map<String, Object> allLinksMap = new HashMap<String, Object>();
		for (String link : allLinks) {
			allLinksMap.put(link, null);
		}
		Map<String, Object> allLowercaseTitlesMap = new HashMap<String, Object>();
		for (String title : allLowercaseTitles) {
			allLowercaseTitlesMap.put(title, null);
		}
		for (Blog blog : blogs) {
			blogService.saveItems(blog, allLinksMap, allLowercaseTitlesMap);
		}
		blogService.setLastIndexedDateFinish(new Date());
	}

	/**
	 * Remove too old items without any clicks ... nobody will see them anyway.
	 */
	// one day = 60 * 60 * 24 * 1000
	@Scheduled(initialDelay = 60 * 60 * 12 * 1000, fixedDelay = 60 * 60 * 24 * 1000)
	@CacheEvict(value = "itemCount", allEntries = true)
	public void cleanOldItems() {
		List<Item> items = itemRepository.findAll();
		for (Item item : items) {
			if (item.getClickCount() == 0 && itemService.isTooOld(item.getPublishedDate())) {
				itemRepository.delete(item);
			}
		}
	}

	int[] getPreviousWeekAndYear(Date date) throws ParseException {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		int year = calendar.get(Calendar.YEAR);
		if (calendar.get(Calendar.WEEK_OF_YEAR) > 1) {
			week = week - 1;
		} else {
			year = year - 1;
			Calendar c = Calendar.getInstance();
			c.setMinimalDaysInFirstWeek(7);
			DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			c.setTime(sdf.parse("31/12/" + year));
			week = c.get(Calendar.WEEK_OF_YEAR);
		}
		return new int[] { week, year };
	}

	/**
	 * Generate best of weekly news
	 */
	@Scheduled(fixedDelay = 60 * 60 * 1000)
	public void addWeeklyNews() throws ParseException {
		System.out.println(">>>>>>>>>>>>>>>>> add weekly news <<<<<<<<<<<<<<<");
		final int[] weekAndYear = getPreviousWeekAndYear(new Date());
		final int week = weekAndYear[0];
		final int year = weekAndYear[1];
		String currentWeekShortTitle = "best-of-" + week + "-" + year;
		NewsItem newsItem = newsItemRepository.findByShortName(currentWeekShortTitle);
		if (newsItem == null) {
			newsItem = new NewsItem();
			Configuration configuration = configurationService.find();
			newsItem.setTitle(configuration.getChannelTitle() + " Weekly: Best of " + week + "/" + year);
			newsItem.setShortName(currentWeekShortTitle);
			newsItem.setShortDescription("Best of " + configuration.getChannelTitle() + ", year " + year + ", week " + week);
			String description = "<p>" + configuration.getChannelTitle() + " brings you interesting news every day.";
			description += " Each week I select the best of:</p>";
			List<Category> categories = categoryService.findAll();
			for (Category category : categories) {
				description += "<h4>" + category.getName() + "</h4>";
				description += "<ul>";
				List<ItemDto> dtoItems = itemService.getDtoItems(0, false, OrderType.MOST_VIEWED, MaxType.WEEK, new Integer[] { category.getId() });
				for (int i = 0; i < dtoItems.size() && i < 5; i++) {
					ItemDto itemDto = dtoItems.get(i);
					description += "<li>";
					description += "<a href='" + itemDto.getLink() + "' target='_blank'>";
					description += itemDto.getTitle();
					description += "</a>";
					description += "</li>";
				}
				description += "</ul>";
			}
			newsItem.setDescription(description);
			newsService.save(newsItem);
		}
	}

}
