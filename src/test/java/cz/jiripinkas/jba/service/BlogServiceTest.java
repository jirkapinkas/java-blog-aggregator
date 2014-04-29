package cz.jiripinkas.jba.service;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlogServiceTest {

	private BlogService blogService;

	@Before
	public void setUp() throws Exception {
		blogService = new BlogService();
	}

	@Test
	public void testGetLastIndexDateMinutes() {
		blogService.setLastIndexedDateFinish(new Date());
		assertEquals(blogService.getLastIndexDateMinutes(), 0);
	}

	@Test
	public void testGetLastIndexDateMinutesEmptyDateFinish() {
		assertEquals(blogService.getLastIndexDateMinutes(), 0);
	}
}
