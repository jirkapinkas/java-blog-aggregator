package cz.jiripinkas.jba.service.scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScheduledTasksServiceTest {
	
	private ScheduledTasksService scheduledTasksService;
	
	@Before
	public void setup() {
		scheduledTasksService = new ScheduledTasksService();
	}

	@Test
	public void shouldReturnLastWeekAndLastYear() throws ParseException {
		Date firstDayOfYear = new Date(new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2015").getTime());
		int[] weekAndYear = scheduledTasksService.getPreviousWeekAndYear(firstDayOfYear);
		Assert.assertEquals(52, weekAndYear[0]);
		Assert.assertEquals(2014, weekAndYear[1]);
	}
	
	@Test
	public void shouldReturnPreviousWeek() throws ParseException {
		Date firstDayOfYear = new Date(new SimpleDateFormat("dd.MM.yyyy").parse("08.01.2015").getTime());
		int[] weekAndYear = scheduledTasksService.getPreviousWeekAndYear(firstDayOfYear);
		Assert.assertEquals(1, weekAndYear[0]);
		Assert.assertEquals(2015, weekAndYear[1]);
	}
	

}
