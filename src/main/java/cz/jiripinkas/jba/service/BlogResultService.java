package cz.jiripinkas.jba.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.repository.BlogRepository;

@Service
public class BlogResultService {

	@Autowired
	private BlogRepository blogRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveOk(Blog blog) {
		blogRepository.saveOk(blog.getId());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveLastIndexedDate(Blog blog) {
		blogRepository.saveLastIndexedDate(blog.getId());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveFail(Blog blog, String errorText) {
		int errorCount = 0;
		if (blog.getLastCheckErrorCount() != null) {
			errorCount = blog.getLastCheckErrorCount();
		}
		errorCount++;
		blogRepository.saveFail(blog.getId(), errorCount, errorText);
	}

}
