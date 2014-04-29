package cz.jiripinkas.jba.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
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

	public List<ItemDto> getDtoItems(int page, boolean showAll) {
		ArrayList<ItemDto> result = new ArrayList<ItemDto>();

		List<Item> items = null;
		if (showAll) {
			items = itemRepository.findPageAllItems(new PageRequest(page, 10, Direction.DESC, "publishedDate"));
		} else {
			items = itemRepository.findPageEnabled(new PageRequest(page, 10, Direction.DESC, "publishedDate"));
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
	@Scheduled(fixedDelay = 86400000)
	public void cleanOldItems() {
		List<Item> items = itemRepository.findAll();
		for (Item item : items) {
			if (isTooOld(item.getPublishedDate())) {
				itemRepository.delete(item);
			}
		}
	}

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
		if(item.getClickCount() == null) {
			item.setClickCount(0);
		}
		item.setClickCount(item.getClickCount() + 1);
		return item.getClickCount();
	}

}
