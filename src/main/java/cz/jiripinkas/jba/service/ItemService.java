package cz.jiripinkas.jba.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringEscapeUtils;
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

	@PersistenceContext
	private EntityManager entityManager;

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
		// call getDtoItems without search
		return getDtoItems(page, showAll, orderType, maxType, selectedCategories, null);
	}

	@Transactional
	public List<ItemDto> getDtoItems(int page, boolean showAll, OrderType orderType, MaxType maxType, Integer[] selectedCategories, String search) {
		Direction orderDirection = Direction.DESC;

		String orderByProperty = null;
		switch (orderType) {
		case LATEST:
			orderByProperty = "i.publishedDate";
			break;
		case MOST_VIEWED:
			orderByProperty = "i.likeCount + ((log(i.clickCount + 1) * 10) + (log(i.twitterRetweetCount + 1) * 10) + (log(i.facebookShareCount + 1) * 10) + (log(i.linkedinShareCount + 1) * 10))";
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

		if (selectedCategories.length == 0) {
			return result;
		}

		String hql = "select i from Item i join fetch i.blog b left join fetch b.category cat where ";
		if (!showAll) {
			hql += " i.enabled = true and ";
		}
		hql += " i.publishedDate >= ?1 and cat.id IN ?2 ";
		if (search != null) {
			for (String string : search.split(" ")) {
				String searchStringPart = StringEscapeUtils.escapeSql(string).trim();
				if (!searchStringPart.isEmpty()) {
					hql += " and (lower(i.title) like lower('%" + searchStringPart + "%') "
							+ " or lower(i.description) like lower('%" + searchStringPart + "%') "
							+ " or lower(b.name) like lower('%" + searchStringPart + "%') "
							+ " or lower(b.nick) like lower('%" + searchStringPart + "%')) ";
				}
			}
		}
		hql += " order by ";
		hql += " " + orderByProperty + " ";
		hql += orderDirection;
		TypedQuery<Item> query = entityManager.createQuery(hql, Item.class).setParameter(1, publishedDate).setParameter(2, Arrays.asList(selectedCategories));
		items = query.setFirstResult(page * 10).setMaxResults(10).getResultList();

		for (Item item : items) {
			ItemDto itemDto = mapper.map(item, ItemDto.class);
			// calculate like count
			itemDto.setDisplayLikeCount(calculateDisplayLikeCount(item));
			result.add(itemDto);
		}
		return result;
	}

	private int calculateDisplayLikeCount(Item item) {
		return (int) (item.getLikeCount() + ((Math.log10(item.getClickCount() + 1)) * 10) + (Math.log10(item.getFacebookShareCount() + 1) * 10) + (Math.log10(item.getTwitterRetweetCount() + 1) * 10) + (Math
				.log10(item.getLinkedinShareCount() + 1) * 10));
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
		return calculateDisplayLikeCount(itemRepository.findOne(itemId));
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
			itemDto.setDisplayLikeCount(calculateDisplayLikeCount(item));
			result.add(itemDto);
		}
		return result;
	}

}
