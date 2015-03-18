package cz.jiripinkas.jba.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.entity.Item;
import cz.jiripinkas.jba.entity.User;
import cz.jiripinkas.jba.repository.BlogRepository;
import cz.jiripinkas.jba.repository.ItemRepository;
import cz.jiripinkas.jba.repository.UserRepository;
import cz.jiripinkas.jba.util.MyUtil;

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

	@Autowired
	private BlogResultService blogResultService;

	private Date lastIndexedDateFinish;

	public void saveItems(Blog blog) {
		StringBuilder errors = new StringBuilder();
		try {
			List<Item> items = rssService.getItems(blog.getUrl(), blog.getId());
			for (Item item : items) {
				if (item.getError() != null) {
					errors.append(item.getError());
					errors.append(", ");
					continue;
				}
				if (!itemService.isTooOld(item.getPublishedDate())) {
					// search for duplicities in the database
					boolean duplicate = false;
					if (itemRepository.findByBlogAndLink(blog, item.getLink()) != null) {
						duplicate = true;
					}
					if (itemRepository.findByBlogAndTitleIgnoreCase(blog, item.getTitle()) != null) {
						duplicate = true;
					}
					if (Boolean.TRUE.equals(blog.getAggregator()) && itemRepository.findByTitleIgnoreCase(item.getTitle()) != null) {
						duplicate = true;
					}
					if (!duplicate) {
						item.setBlog(blog);
						itemRepository.save(item);
					}
				}
			}
			blogResultService.saveOk(blog);
		} catch (Exception e) {
			System.out.println("exception during downloading: " + blog.getUrl());
			System.out.println("message: " + e.getMessage());
			errors.append(e.getMessage());
		}
		if (errors.length() != 0) {
			blogResultService.saveFail(blog, errors.toString());
		}
	}

	public int getLastIndexDateMinutes() {
		if (lastIndexedDateFinish == null) {
			return 0;
		}
		return (int) ((new Date().getTime() - lastIndexedDateFinish.getTime()) / (1000 * 60));
	}

	@CacheEvict(value = "blogCount", allEntries = true)
	@Async
	public void save(Blog blog, String name) {
		User user = userRepository.findByName(name);
		blog.setUser(user);
		blog.setShortName(MyUtil.generatePermalink(blog.getShortName()));
		blogRepository.save(blog);
		saveItems(blog);
	}

	@Transactional
	public void update(Blog blog, String username, boolean isAdmin) {
		Blog managedBlog = blogRepository.findOne(blog.getId());
		if (!managedBlog.getUser().getName().equals(username) && !isAdmin) {
			throw new AccessDeniedException("user attempted to edit another user's blog");
		}
		managedBlog.setName(blog.getName());
		managedBlog.setShortName(blog.getShortName());
		managedBlog.setUrl(blog.getUrl());
		managedBlog.setHomepageUrl(blog.getHomepageUrl());
		managedBlog.setAggregator(blog.getAggregator());
		blogRepository.save(managedBlog);
	}

	@Caching(evict = { @CacheEvict(value = "blogCount", allEntries = true), @CacheEvict(value = "icons", allEntries = true) })
	@Transactional
	@PreAuthorize("#blog.user.name == authentication.name or hasRole('ROLE_ADMIN')")
	public void delete(@P("blog") Blog blog) {
		blogRepository.delete(blog);
	}

	@Transactional
	public Blog findOne(int id) {
		return blogRepository.findOne(id);
	}

	@Cacheable("icons")
	@Transactional
	public byte[] getIcon(int blogId) throws IOException {
		byte[] icon = blogRepository.findOne(blogId).getIcon();
		if (icon == null) {
			return IOUtils.toByteArray(getClass().getResourceAsStream("/generic-blog.png"));
		}
		return icon;
	}

	public Blog findOne(String url) {
		return blogRepository.findByUrl(url);
	}

	@Transactional
	public Blog findOneFetchUser(int id) {
		return blogRepository.findOneFetchUser(id);
	}

	@Cacheable("blogCount")
	public long count() {
		return blogRepository.count();
	}

	public void setLastIndexedDateFinish(Date lastIndexedDateFinish) {
		this.lastIndexedDateFinish = lastIndexedDateFinish;
	}

	@CacheEvict(value = "icons", allEntries = true)
	public void saveIcon(int blogId, byte[] bytes) {
		Blog blog = blogRepository.findOne(blogId);
		blog.setIcon(bytes);
		blogRepository.save(blog);
	}

	@Transactional
	public Blog findByShortName(String shortName) {
		return blogRepository.findByShortName(shortName);
	}

	@Transactional
	public List<Blog> findAll() {
		return blogRepository.findAllFetchUser();
	}

}