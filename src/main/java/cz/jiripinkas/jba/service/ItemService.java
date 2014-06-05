package cz.jiripinkas.jba.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
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

	public List<ItemDto> getDtoItems(int page, boolean showAll, OrderType orderType, MaxType maxType) {
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
			items = itemRepository.findPageAllItems(publishedDate, new PageRequest(page, 10, orderDirection, orderByProperty));
		} else {
			items = itemRepository.findPageEnabled(publishedDate, new PageRequest(page, 10, orderDirection, orderByProperty));
		}
		for (Item item : items) {
			ItemDto itemDto = mapper.map(item, ItemDto.class);
			result.add(itemDto);
		}
		return result;
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

	// 86400000 = one day = 60 * 60 * 24 * 1000
	// @Scheduled(fixedDelay = 86400000)
	// public void cleanOldItems() {
	// List<Item> items = itemRepository.findAll();
	// for (Item item : items) {
	// if (isTooOld(item.getPublishedDate())) {
	// itemRepository.delete(item);
	// }
	// }
	// }

	@Transactional
	public boolean toggleEnabled(int id) {
		Item item = itemRepository.findOne(id);
		item.setEnabled(!item.isEnabled());
		return item.isEnabled();
	}

	public long count() {
		return itemRepository.count();
	}

	@Transactional
	public int incCount(int id) {
		Item item = itemRepository.findOne(id);
		item.setClickCount(item.getClickCount() + 1);
		return item.getClickCount();
	}

}
