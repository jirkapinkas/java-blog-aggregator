package cz.jiripinkas.jba.service;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

public class ItemServiceTest {

	private ItemService itemService;

	@Before
	public void setUp() throws Exception {
		itemService = new ItemService();
	}

	@Test
	public void testIsTooOld() {
		assertFalse(itemService.isTooOld(new Date()));
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.MONTH, -5);
		assertTrue(itemService.isTooOld(calendar.getTime()));
	}

}
