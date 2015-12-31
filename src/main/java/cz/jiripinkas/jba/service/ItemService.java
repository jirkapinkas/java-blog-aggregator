package cz.jiripinkas.jba.service;

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
	private AllCategoriesService allCategoriesService;

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
		return getDtoItems(page, showAll, orderType, maxType, selectedCategories, null, null);
	}

	@Transactional
	public List<ItemDto> getDtoItems(int page, boolean showAll, OrderType orderType, MaxType maxType, Integer[] selectedCategories, String search) {
		// call getDtoItems without search
		return getDtoItems(page, showAll, orderType, maxType, selectedCategories, search, null);
	}
	
	/**
	 * Returns from which date will be items retrieved (current date - date interval)
	 * @param maxType Date interval or null if undefined
	 * @return Date
	 */
	private Date getLastSavedDate(MaxType maxType) {
		Date savedDate = null;
		switch (maxType) {
		case UNDEFINED:
			savedDate = null;
			break;

		case MONTH:
			GregorianCalendar calendar1 = new GregorianCalendar();
			calendar1.add(Calendar.MONTH, -1);
			savedDate = calendar1.getTime();
			break;

		case WEEK:
			GregorianCalendar calendar2 = new GregorianCalendar();
			calendar2.add(Calendar.WEEK_OF_MONTH, -1);
			savedDate = calendar2.getTime();
			break;
		}
		return savedDate;
	}

	@Transactional
	public List<ItemDto> getDtoItems(int page, boolean showAll, OrderType orderType, MaxType maxType, Integer[] selectedCategories, String search, String blogShortName) {
		if (selectedCategories == null) {
			selectedCategories = allCategoriesService.getAllCategoryIds();
		}
		Direction orderDirection = Direction.DESC;

		String orderByProperty = null;
		switch (orderType) {
		case LATEST:
			orderByProperty = "i.savedDate";
			break;
		case MOST_VIEWED:
			orderByProperty = "i.likeCount + ((log(i.clickCount + 1) * 10) + (log(i.twitterRetweetCount + 1) * 10) + (log(i.facebookShareCount + 1) * 3) + (log(i.linkedinShareCount + 1) * 10))";
			break;
		}

		ArrayList<ItemDto> result = new ArrayList<>();

		List<Item> items = null;

		if (selectedCategories.length == 0) {
			return result;
		}

		String hql = "select i from Item i join fetch i.blog b left join fetch b.category cat where 1=1 ";
		if (!showAll) {
			hql += " and i.enabled = true ";
		}
		if(maxType != MaxType.UNDEFINED) {
			hql += " and i.savedDate >= :maxSavedDate ";
		}
		
		// construct query for selected categories
		hql += " and (cat.id in (:selectedCategories) ";
		if(showAll) {
			// add "null" to selectedCategories, otherwise items from those categories wouldn't be shown.
			// It's necessary so that an administrator can see items from blogs which have no category
			hql += " or cat.id is null ";
		}
		hql += ") ";
		
		List<String> parameterNames = new ArrayList<>();
		List<String> parameterValues = new ArrayList<>();
		if (search != null) {
			String[] strings = search.split(" ");
			for(int i = 0; i < strings.length; i++) {
				String string = strings[i];
				String searchStringPart = StringEscapeUtils.escapeSql(string).trim();
				String[] stopWords = new String[] {"a", "an", "and", "are", "as", "at", 
						"be", "but", "by","for", "if", "in", "into", "is", "it","no", "not", "of", "on", "or", 
						"such","that", "the", "their", "then", "there", "these","they", "this", "to", "was", "will", 
						"with", "..."};
				if(searchStringPart.isEmpty() || Arrays.asList(stopWords).contains(searchStringPart)) {
					// ignore
				} else {
					parameterNames.add("searchParamTitle" + i);
					parameterValues.add("%" + searchStringPart + "%");
					parameterNames.add("searchParamDescription" + i);
					parameterValues.add("%" + searchStringPart + "%");
					parameterNames.add("searchParamName" + i);
					parameterValues.add("%" + searchStringPart + "%");
					parameterNames.add("searchParamNick" + i);
					parameterValues.add("%" + searchStringPart + "%");
					hql += " and (lower(i.title) like lower(:searchParamTitle" + i + ") ";
					hql += " or lower(i.description) like lower(:searchParamDescription" + i + ") "; 
					hql += " or lower(b.name) like lower(:searchParamName" + i + ") ";
					hql += " or lower(b.nick) like lower(:searchParamNick" + i + ")) ";
				}
			}
		}
		if (blogShortName != null) {
			hql += " and b.shortName = :blogShortName ";
		}
		hql += " order by ";
		hql += " " + orderByProperty + " ";
		hql += orderDirection;
		TypedQuery<Item> query = entityManager.createQuery(hql, Item.class);
		if(maxType != MaxType.UNDEFINED) {
			Date savedDate = getLastSavedDate(maxType);
			query.setParameter("maxSavedDate", savedDate);
		}
		query.setParameter("selectedCategories", Arrays.asList(selectedCategories));
		
		for(int i = 0; i < parameterNames.size(); i++) {
			query.setParameter(parameterNames.get(i), parameterValues.get(i));
		}
		
		if (blogShortName != null) {
			String blogShortNameEscaped = StringEscapeUtils.escapeSql(blogShortName);
			query.setParameter("blogShortName", blogShortNameEscaped);
		}
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

	public boolean isTooOldOrYoung(Date date) {
		GregorianCalendar calendar1 = new GregorianCalendar();
		calendar1.add(Calendar.MONTH, -2);
		Date oneMonthBefore = calendar1.getTime();
		GregorianCalendar calendar2 = new GregorianCalendar();
		calendar2.add(Calendar.DATE, 1);
		Date oneDayAfter = calendar2.getTime();
		if (date.compareTo(oneMonthBefore) < 0) {
			return true;
		}
		if (date.compareTo(oneDayAfter) > 0) {
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

}
