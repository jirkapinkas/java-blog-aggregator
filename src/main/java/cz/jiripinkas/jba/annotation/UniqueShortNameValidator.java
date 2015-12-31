package cz.jiripinkas.jba.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import cz.jiripinkas.jba.repository.BlogRepository;
import cz.jiripinkas.jba.util.MyUtil;

public class UniqueShortNameValidator implements ConstraintValidator<UniqueShortName, String> {
	
	@Autowired
	private BlogRepository blogRepository;

	@Override
	public void initialize(UniqueShortName constraintAnnotation) {
	}

	@Override
	public boolean isValid(String shortName, ConstraintValidatorContext context) {
		if(blogRepository == null) {
			return true;
		}
		return blogRepository.findByShortName(MyUtil.generatePermalink(shortName)) == null;
	}

}
