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
		blog.setLastCheckErrorText(null);
		blog.setLastCheckErrorCount(0);
		blog.setLastCheckStatus(true);
		blogRepository.save(blog);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveFail(Blog blog, String errorText) {
		blog.setLastCheckErrorText(errorText);
		int errorCount = 0;
		if(blog.getLastCheckErrorCount() != null) {
			errorCount = blog.getLastCheckErrorCount();
		}
		blog.setLastCheckErrorCount(errorCount + 1);
		blog.setLastCheckStatus(false);
		blogRepository.save(blog);
	}

}
