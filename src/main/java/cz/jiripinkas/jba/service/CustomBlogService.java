package cz.jiripinkas.jba.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import cz.jiripinkas.jba.entity.CustomBlog;
import cz.jiripinkas.jba.repository.CustomBlogRepository;
import cz.jiripinkas.jba.util.MyUtil;

@Service
public class CustomBlogService {

	private static final int PAGE_SIZE = 10;

	@Autowired
	private CustomBlogRepository customBlogRepository;

	public void save(CustomBlog customBlog) {
		customBlog.setPublishedDate(new Date());
		if (customBlog.getShortName() == null || customBlog.getShortName().isEmpty()) {
			customBlog.setShortName(MyUtil.generatePermalink(customBlog.getTitle()));
		}
		customBlogRepository.save(customBlog);
	}

	public Page<CustomBlog> findBlogs(int page) {
		return customBlogRepository.findAll(new PageRequest(page, PAGE_SIZE, Direction.DESC, "publishedDate"));
	}

	public CustomBlog findOne(String shortName) {
		return customBlogRepository.findByShortName(shortName);
	}

}
