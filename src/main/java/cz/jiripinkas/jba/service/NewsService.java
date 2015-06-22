package cz.jiripinkas.jba.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.jiripinkas.jba.entity.Configuration;
import cz.jiripinkas.jba.entity.NewsItem;
import cz.jiripinkas.jba.repository.NewsItemRepository;
import cz.jiripinkas.jba.rss.TRss;
import cz.jiripinkas.jba.rss.TRssChannel;
import cz.jiripinkas.jba.rss.TRssItem;
import cz.jiripinkas.jba.util.MyUtil;

@Service
public class NewsService {

	private static final int PAGE_SIZE = 10;

	@Autowired
	private NewsItemRepository newsItemRepository;

	@Autowired
	private ConfigurationService configurationService;

	public void save(NewsItem newsItem) {
		newsItem.setPublishedDate(new Date());
		if (newsItem.getShortName() == null || newsItem.getShortName().isEmpty()) {
			newsItem.setShortName(MyUtil.generatePermalink(newsItem.getTitle()));
		}
		newsItemRepository.save(newsItem);
	}

	public Page<NewsItem> findBlogs(int page) {
		return newsItemRepository.findAll(new PageRequest(page, PAGE_SIZE, Direction.DESC, "publishedDate"));
	}
	
	public List<NewsItem> findAll() {
		return newsItemRepository.findAll();
	}

	@Transactional
	public NewsItem findOne(String shortName) {
		return newsItemRepository.findByShortName(shortName);
	}

	public String dateToString(Date date) {
		return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).format(date);
	}

	public TRss getFeed() {
		Configuration configuration = configurationService.find();
		Page<NewsItem> firstTenNews = findBlogs(0);
		TRss rss = new TRss();
		List<TRssItem> rssItems = new ArrayList<TRssItem>();

		for (NewsItem newsItem : firstTenNews.getContent()) {
			TRssItem rssItem = new TRssItem();
			rssItem.setTitle(newsItem.getTitle());
			rssItem.setDescription(newsItem.getShortDescription());
			rssItem.setLink(configuration.getChannelLink() + "/news/" + newsItem.getShortName() + ".html");
			rssItem.setPubDate(dateToString(newsItem.getPublishedDate()));
			rssItems.add(rssItem);
		}

		TRssChannel rssChannel = new TRssChannel();
		rssChannel.setTitle(configuration.getChannelTitle());
		rssChannel.setDescription(configuration.getChannelDescription());
		rssChannel.setLink(configuration.getChannelLink());
		if (firstTenNews.getContent().size() > 0) {
			rssChannel.setLastBuildDate(dateToString(firstTenNews.getContent().get(0).getPublishedDate()));
		}
		rssChannel.setItems(rssItems);
		rss.setChannels(Arrays.asList(new TRssChannel[] { rssChannel }));
		return rss;
	}

	public void delete(int id) {
		newsItemRepository.delete(id);
	}

}
