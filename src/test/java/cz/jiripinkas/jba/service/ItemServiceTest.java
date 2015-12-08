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
	public void testIsTooOldOrYoung() {
		assertFalse(itemService.isTooOldOrYoung(new Date()));
		Calendar calendar1 = new GregorianCalendar();
		calendar1.add(Calendar.MONTH, -5);
		assertTrue(itemService.isTooOldOrYoung(calendar1.getTime()));
		Calendar calendar2 = new GregorianCalendar();
		calendar2.add(Calendar.DATE, 1);
		calendar2.add(Calendar.MINUTE, 1);
		assertTrue(itemService.isTooOldOrYoung(calendar2.getTime()));
	}

}
