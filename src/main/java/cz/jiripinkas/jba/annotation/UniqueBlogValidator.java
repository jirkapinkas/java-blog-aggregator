package cz.jiripinkas.jba.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import cz.jiripinkas.jba.repository.BlogRepository;

public class UniqueBlogValidator implements ConstraintValidator<UniqueBlog, String> {
	
	@Autowired
	private BlogRepository blogRepository;

	@Override
	public void initialize(UniqueBlog constraintAnnotation) {
	}

	@Override
	public boolean isValid(String url, ConstraintValidatorContext context) {
		if(blogRepository == null) {
			return true;
		}
		return blogRepository.findByUrl(url) == null;
	}

}
