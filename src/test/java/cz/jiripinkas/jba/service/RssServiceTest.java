package cz.jiripinkas.jba.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cz.jiripinkas.jba.entity.Item;
import cz.jiripinkas.jba.exception.RssException;
import cz.jiripinkas.jba.exception.UrlException;

public class RssServiceTest {

	private RssService rssService;

	@Before
	public void setUp() throws Exception {
		rssService = new RssService();
	}

	@Test
	public void testGetItemsFileJavaVids() throws RssException {
		List<Item> items = rssService.getItems("test-rss/javavids.xml", true);
		assertEquals(10, items.size());
		Item firstItem = items.get(0);
		assertEquals("How to generate web.xml in Eclipse", firstItem.getTitle());
		assertEquals("23 03 2014 09:01:34", new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(firstItem.getPublishedDate()));
	}

	@Test
	public void testGetItemsFileSpring() throws RssException {
		List<Item> items = rssService.getItems("test-rss/spring.xml", true);
		assertEquals(20, items.size());
		Item firstItem = items.get(0);
		assertEquals("Spring Boot 1.0.1.RELEASE Available Now", firstItem.getTitle());
		assertEquals("https://spring.io/blog/2014/04/07/spring-boot-1-0-1-release-available-now", firstItem.getLink());
		assertEquals("07 04 2014 10:14:00", new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(firstItem.getPublishedDate()));
		if (!firstItem.getDescription().startsWith("Spring Boot 1.0.1.RELEASE is available")) {
			fail("description does not match");
		}
	}

	@Test
	public void testGetItemsFileHibernate() throws RssException {
		List<Item> items = rssService.getItems("test-rss/hibernate.xml", true);
		assertEquals(14, items.size());
		Item firstItem = items.get(0);
		assertEquals("Third milestone on the path for Hibernate Search 5", firstItem.getTitle());
		assertEquals("http://in.relation.to/Bloggers/ThirdMilestoneOnThePathForHibernateSearch5", firstItem.getLink());
		assertEquals("04 04 2014 17:20:32", new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(firstItem.getPublishedDate()));
		if (!firstItem.getDescription().startsWith("Version 5.0.0.Alpha3 is now available")) {
			fail("description does not match");
		}
	}

	@Test
	public void testPullLinks() {
		String inputText = "Hibernate ORM 4.2.12.Final was just released! Please see the full changelog for more information: https://hibernate.atlassian.net/secure/ReleaseNote.jspa?projectId=10031&version=16350. "
				+ "Maven Central: http://repo1.maven.org/maven2/org/hibernate/hibernate-core (should update in a couple of days) "
				+ "SourceForge: www.sourceforge.net/projects/hibernate/files/hibernate4 "
				+ "JBoss Nexus: ftp://repository.jboss.org/nexus/content/groups/public/org/hibernate "
				+ " mailto:test@email.com";
		ArrayList<String> links = rssService.pullLinks(inputText);
		assertEquals(5, links.size());
		assertEquals(links.get(0), "https://hibernate.atlassian.net/secure/ReleaseNote.jspa?projectId=10031&version=16350");
		assertEquals(links.get(1), "http://repo1.maven.org/maven2/org/hibernate/hibernate-core");
		assertEquals(links.get(2), "www.sourceforge.net/projects/hibernate/files/hibernate4");
		assertEquals(links.get(3), "ftp://repository.jboss.org/nexus/content/groups/public/org/hibernate");
		assertEquals(links.get(4), "mailto:test@email.com");
	}

	@Test
	public void testCleanTitle() {
		assertEquals("test", rssService.cleanTitle("   test   "));
		assertEquals("Pat & Mat", rssService.cleanTitle("Pat & Mat"));
		assertEquals("link:", rssService.cleanTitle("link: <a href='http://something.com'></a>"));
		assertEquals("script:", rssService.cleanTitle("script: <script><!-- alert('hello'); --></script>"));
	}

	@Test
	public void testGetRssDate() throws ParseException {
		assertEquals("Sun Mar 23 09:01:34 CET 2014", rssService.getRssDate("Sun, 23 Mar 2014 08:01:34 +0000").toString());
		assertEquals("Sun Mar 23 00:00:00 CET 2014", rssService.getRssDate("Sun, 23 Mar 2014").toString());
		assertEquals("Sun Jan 25 21:00:00 CET 2015", rssService.getRssDate("25 Jan 2015 20:00:00 GMT").toString());
	}

	@Test
	public void testFixDate() {
		String fixDate = rssService.fixDate("<guid>http://www.jsfcentral.com/listings/R221940</guid><pubDate>    25 Jan 2015 20:00:00 GMT   </pubDate><description>");
		assertEquals("<guid>http://www.jsfcentral.com/listings/R221940</guid><pubDate>25 Jan 2015 20:00:00 GMT</pubDate><description>", fixDate);
	}

	@Test
	public void testCleanDescription() {
		assertEquals("test this is strong", rssService.cleanDescription("test <strong>this is strong</strong>"));
		assertEquals("test this is strong", rssService.cleanDescription("test &lt;strong&gt;this is strong&lt;/strong&gt;"));
		assertEquals("test this is strong", rssService.cleanDescription("<![CDATA[test &lt;strong&gt;this is strong&lt;/strong&gt;]]>"));
		assertEquals("stupid description", rssService.cleanDescription("~~~~~~~~ stupid description ~~~~~~~~"));
		assertEquals(
				"This tutorial will show example codes on how to convert Java String To Long. This is a common scenario when programming with Core Java. [......",
				rssService
						.cleanDescription("<![CDATA[ This tutorial will show example codes on how to convert &lt;strong&gt;Java String To Long&lt;/strong&gt;. This is a common scenario when programming with Core Java. <a href='http://javadevnotes.com/java-string-to-long-examples' class='excerpt-more'>[...]</a> ]]>"));
		assertEquals("test", rssService.cleanDescription("test <script> alert('hello')</script>"));
	}

	@Test
	public void testGetItemsFileInstanceofJavaPublishedDate() throws RssException {
		List<Item> items = rssService.getItems("test-rss/instanceofjava.xml", true);
		Item firstItem = items.get(0);
		assertEquals("22 02 2015 13:35:00", new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(firstItem.getPublishedDate()));
		assertEquals("http://www.instanceofjava.com/2015/02/java-8-interface-static-default-methods.html", firstItem.getLink());
	}

	@Test
	public void testGetItemsFileBaeldungFeedburnerOrigLink() throws RssException {
		List<Item> items = rssService.getItems("test-rss/baeldung.xml", true);
		Item firstItem = items.get(0);
		assertEquals("http://www.baeldung.com/spring-security-oauth2-authentication-with-reddit", firstItem.getLink());
	}

	@Test
	public void testRedirect() throws Exception {
		String realLink = rssService.getRealLink("http://www.java-skoleni.cz/skoleni.php?id=java");
		assertEquals("http://www.java-skoleni.cz/kurz/java", realLink);
	}

	@Test(expected = UrlException.class)
	public void test404() throws Exception {
		rssService.getRealLink("http://www.java-skoleni.cz/xxxx");
	}

	@Test
	public void test200() throws Exception {
		String realLink = rssService.getRealLink("http://www.java-skoleni.cz");
		assertEquals("http://www.java-skoleni.cz", realLink);
	}

	@Test
	public void testWhitespaces() throws Exception {
		String realLink = rssService.getRealLink("   http://www.java-skoleni.cz   ");
		assertEquals("http://www.java-skoleni.cz", realLink);
	}

}
