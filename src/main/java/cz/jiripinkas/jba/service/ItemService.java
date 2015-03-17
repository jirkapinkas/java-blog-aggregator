package cz.jiripinkas.jba.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.jiripinkas.jba.dto.ItemDto;
import cz.jiripinkas.jba.entity.Item;
import cz.jiripinkas.jba.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private Mapper mapper;

	public enum OrderType {
		LATEST, MOST_VIEWED
	};

	public enum MaxType {
		WEEK, MONTH, UNDEFINED
	}

	@Transactional
	public List<ItemDto> getDtoItems(int page, boolean showAll, OrderType orderType, MaxType maxType, Integer[] selectedCategories) {
		Direction orderDirection = Direction.DESC;

		String orderByProperty = null;
		switch (orderType) {
		case LATEST:
			orderByProperty = "publishedDate";
			break;
		case MOST_VIEWED:
			orderByProperty = "clickCount";
			break;
		}

		Date publishedDate = null;
		switch (maxType) {
		case UNDEFINED:
			try {
				publishedDate = new SimpleDateFormat("dd.MM.yyyy").parse("01.01.1970");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;

		case MONTH:
			GregorianCalendar calendar1 = new GregorianCalendar();
			calendar1.add(Calendar.MONTH, -1);
			publishedDate = calendar1.getTime();
			break;

		case WEEK:
			GregorianCalendar calendar2 = new GregorianCalendar();
			calendar2.add(Calendar.WEEK_OF_MONTH, -1);
			publishedDate = calendar2.getTime();
			break;
		}

		ArrayList<ItemDto> result = new ArrayList<ItemDto>();

		List<Item> items = null;

		if (showAll) {
			items = itemRepository.findPageAllItemsInCategory(publishedDate, Arrays.asList(selectedCategories), new PageRequest(page, 10, orderDirection, orderByProperty));
		} else {
			items = itemRepository.findPageEnabledInCategory(publishedDate, Arrays.asList(selectedCategories), new PageRequest(page, 10, orderDirection, orderByProperty));
		}
		for (Item item : items) {
			ItemDto itemDto = mapper.map(item, ItemDto.class);
			// calculate like count
			itemDto.setLikeCount(calculateLikeCount(itemDto.getLikeCount(), itemDto.getClickCount()));
			result.add(itemDto);
		}
		return result;
	}

	private int calculateLikeCount(int likeCount, int clickCount) {
		return likeCount + (int) (clickCount / 5);
	}

	public boolean isTooOld(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.MONTH, -2);
		Date oneMonthBefore = calendar.getTime();
		if (date.compareTo(oneMonthBefore) < 0) {
			return true;
		}
		return false;
	}

	@Transactional
	public boolean toggleEnabled(int id) {
		Item item = itemRepository.findOne(id);
		item.setEnabled(!item.isEnabled());
		return item.isEnabled();
	}

	@Cacheable("itemCount")
	public long count() {
		return itemRepository.count();
	}

	@Transactional
	public int incCount(int id) {
		itemRepository.incClickCount(id);
		return itemRepository.getClickCount(id);
	}

	@Transactional
	public int incLike(int itemId) {
		return like(itemId, 1);
	}

	@Transactional
	public int decLike(int itemId) {
		return like(itemId, -1);
	}

	private int like(int itemId, int amount) {
		itemRepository.changeLike(itemId, amount);
		Map<String, Integer> likeAndClickCount = itemRepository.getLikeAndClickCount(itemId);
		return calculateLikeCount(likeAndClickCount.get("like"), likeAndClickCount.get("click"));
	}

	@Transactional
	public int incDislike(int itemId) {
		return dislike(itemId, 1);
	}

	@Transactional
	public int decDislike(int itemId) {
		return dislike(itemId, -1);
	}

	private int dislike(int itemId, int amount) {
		itemRepository.changeDislike(itemId, amount);
		return itemRepository.getDislikeCount(itemId);
	}

	@Transactional
	public List<ItemDto> getDtoItems(int page, String blogShortName) {
		ArrayList<ItemDto> result = new ArrayList<ItemDto>();
		List<Item> items = null;
		items = itemRepository.findBlogPageEnabled(blogShortName, new PageRequest(page, 10, Direction.DESC, "publishedDate"));
		for (Item item : items) {
			ItemDto itemDto = mapper.map(item, ItemDto.class);
			result.add(itemDto);
		}
		return result;
	}

	@Transactional
	public List<ItemDto> getCategoryDtoItems(int page, String categoryShortName) {
		ArrayList<ItemDto> result = new ArrayList<ItemDto>();
		List<Item> items = null;
		items = itemRepository.findCategoryPageEnabled(categoryShortName, new PageRequest(page, 10, Direction.DESC, "publishedDate"));
		for (Item item : items) {
			ItemDto itemDto = mapper.map(item, ItemDto.class);
			result.add(itemDto);
		}
		return result;
	}

}
