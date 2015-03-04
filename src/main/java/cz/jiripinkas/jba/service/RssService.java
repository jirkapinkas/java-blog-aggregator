package cz.jiripinkas.jba.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cz.jiripinkas.jba.atom.Entry;
import cz.jiripinkas.jba.atom.Feed;
import cz.jiripinkas.jba.atom.Link;
import cz.jiripinkas.jba.entity.Item;
import cz.jiripinkas.jba.exception.RssException;
import cz.jiripinkas.jba.exception.UrlException;
import cz.jiripinkas.jba.repository.ItemRepository;
import cz.jiripinkas.jba.rss.TRss;
import cz.jiripinkas.jba.rss.TRssChannel;
import cz.jiripinkas.jba.rss.TRssItem;

@Service
public class RssService {

	private static Unmarshaller unmarshallerRss;
	private static Unmarshaller unmarshallerAtom;
	private static DocumentBuilder db;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CloseableHttpClient httpClient;

	static {
		try {
			System.setProperty("com.sun.net.ssl.checkRevocation", "false");
			JAXBContext jaxbContextRss = JAXBContext.newInstance(TRss.class);
			unmarshallerRss = jaxbContextRss.createUnmarshaller();
			JAXBContext jaxbContextAtom = JAXBContext.newInstance(Feed.class);
			unmarshallerAtom = jaxbContextAtom.createUnmarshaller();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Item> getItems(String location, int blogId) throws RssException {
		return getItems(location, false, blogId);
	}

	/**
	 * This method ensures that the output String has only valid XML unicode
	 * characters as specified by the XML 1.0 standard. For reference, please
	 * see <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the
	 * standard</a>. This method will return an empty String if the input is
	 * null or empty.
	 *
	 * @param in
	 *            The String whose non-valid characters we want to remove.
	 * @return The in String, stripped of non-valid characters.
	 */
	private String stripNonValidXMLCharacters(String in) {
		StringBuffer out = new StringBuffer(); // Used to hold the output.
		char current; // Used to reference the current character.

		if (in == null || ("".equals(in)))
			return ""; // vacancy test.
		for (int i = 0; i < in.length(); i++) {
			current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught
									// here; it should not happen.
			if ((current == 0x9) || (current == 0xA) || (current == 0xD) || ((current >= 0x20) && (current <= 0xD7FF)) || ((current >= 0xE000) && (current <= 0xFFFD))
					|| ((current >= 0x10000) && (current <= 0x10FFFF)))
				out.append(current);
		}
		return out.toString();
	}

	/**
	 * fix for jsfcentral atom feed, which contains white space in pubDate
	 */
	protected String fixDate(String page) {
		String result = page.replaceAll("<pubDate>(\\s*)(.*)</pubDate>", "<pubDate>$2</pubDate>");
		result = result.replaceAll("\\s*</pubDate>", "</pubDate>");
		return result;
	}

	private HttpGet constructGet(String location) {
		HttpGet get = new HttpGet(location);
		Builder requestConfigBuilder = RequestConfig.custom().setSocketTimeout(100000).setConnectTimeout(100000);
		get.setConfig(requestConfigBuilder.build());
		get.setHeader("Accept", "application/xml,application/rss+xml,text/html,*/*");
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36");
		return get;
	}

	public List<Item> getItems(String location, boolean localFile, int blogId) throws RssException {
		Node node = null;
		Reader reader = null;

		try {
			Document document = null;
			if (localFile) {
				File file = new File(location);
				document = db.parse(file);
				reader = new StringReader(FileUtils.readFileToString(file));
			} else {
				HttpGet get = constructGet(location);
				CloseableHttpResponse response = null;
				try {
					response = httpClient.execute(get);
					HttpEntity entity = response.getEntity();
					String page = EntityUtils.toString(entity);
					page = page.replace("&ndash;", "-");
					page = stripNonValidXMLCharacters(page);
					page = fixDate(page);
					document = db.parse(new ByteArrayInputStream(page.getBytes(Charset.forName("UTF-8"))));
					reader = new StringReader(page);
				} finally {
					if (response != null) {
						response.close();
					}
				}
			}
			node = document.getDocumentElement();
		} catch (Exception ex) {
			System.out.println("error parsing XML file");
			throw new RssException(ex);
		}

		if ("rss".equals(node.getNodeName())) {
			return getRssItems(reader, blogId);
		} else if ("feed".equals(node.getNodeName())) {
			return getAtomItems(reader, blogId);
		} else {
			throw new RssException("unknown RSS type");
		}
	}

	protected String getRealLink(String link) throws UrlException {
		link = link.trim();
		String realLink = null;
		try {
			HttpGet get = constructGet(link);
			CloseableHttpResponse response = null;
			try {
				HttpClientContext context = HttpClientContext.create();
				response = httpClient.execute(get, context);
				HttpEntity entity = response.getEntity();
				EntityUtils.toString(entity); // consume page
				if (response.getStatusLine().getStatusCode() != 200) {
					throw new UrlException("Link: " + link + " returned: " + response.getStatusLine().getStatusCode());
				}
				if (context.getRedirectLocations() == null) {
					// no redirections performed
					realLink = link;
				} else {
					realLink = context.getRedirectLocations().get(context.getRedirectLocations().size() - 1).toString();
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (Exception e) {
			throw new UrlException("Exception during downloading: " + link);
		}
		return realLink;
	}

	private List<Item> getRssItems(Reader reader, int blogId) throws RssException {
		ArrayList<Item> list = new ArrayList<Item>();
		try {
			TRss rss = (TRss) unmarshallerRss.unmarshal(reader);

			List<TRssChannel> channels = rss.getChannels();

			for (TRssChannel channel : channels) {
				List<TRssItem> items = channel.getItems();
				for (TRssItem rssItem : items) {
					Item item = new Item();
					item.setTitle(cleanTitle(rssItem.getTitle()));
					item.setDescription(cleanDescription(rssItem.getDescription()));
					Date pubDate = getRssDate(rssItem.getPubDate());
					item.setPublishedDate(pubDate);
					String link = null;
					if (rssItem.getOrigLink() != null) {
						link = rssItem.getOrigLink();
					} else {
						link = rssItem.getLink();
					}
					if (itemRepository.findItemIdByLinkAndBlogId(link, blogId) != null) {
						// skip this item, it's already in the database
						continue;
					}
					try {
						item.setLink(getRealLink(link));
					} catch (UrlException e) {
						item.setError(e.getMessage());
					}
					list.add(item);
				}
			}
		} catch (JAXBException | ParseException e) {
			throw new RssException(e);
		}
		return list;
	}

	private List<Item> getAtomItems(Reader reader, int blogId) throws RssException {
		ArrayList<Item> list = new ArrayList<Item>();
		try {
			Feed atom = (Feed) unmarshallerAtom.unmarshal(reader);
			List<Entry> entries = atom.getEntries();
			for (Entry entry : entries) {
				Item item = new Item();
				item.setTitle(cleanTitle(entry.getTitle()));
				String summary = entry.getSummary();
				String description = null;
				if (summary != null && !summary.trim().isEmpty()) {
					description = summary;
				} else {
					description = entry.getContent();
				}
				item.setDescription(cleanDescription(description));
				Date pubDate = null;
				if (entry.getPublished() != null) {
					pubDate = entry.getPublished().toGregorianCalendar().getTime();
				} else {
					pubDate = entry.getUpdated().toGregorianCalendar().getTime();
				}
				item.setPublishedDate(pubDate);
				String link = null;
				if (entry.getOrigLink() != null) {
					link = entry.getOrigLink();
				} else {
					if (entry.getLinks().size() == 1) {
						link = entry.getLinks().get(0).getHref();
					} else {
						for (Link atomLink : entry.getLinks()) {
							if ("alternate".equals(atomLink.getRel())) {
								link = atomLink.getHref();
								break;
							}
						}
					}
				}
				if (itemRepository.findItemIdByLinkAndBlogId(link, blogId) != null) {
					// skip this item, it's already in the database
					continue;
				}
				try {
					item.setLink(getRealLink(link));
				} catch (UrlException e) {
					item.setError(e.getMessage());
				}
				list.add(item);
			}
		} catch (JAXBException e) {
			throw new RssException(e);
		}
		return list;
	}

	public Date getRssDate(String stringDate) throws ParseException {
		try {
			return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(stringDate);
		} catch (ParseException e) {
			try {
				return new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH).parse(stringDate);
			} catch (ParseException e2) {
				return new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(stringDate);
			}
		}
	}

	public String cleanTitle(String title) {
		return Jsoup.parse(title).text();
	}

	public String cleanDescription(String description) {
		String unescapedDescription = StringEscapeUtils.unescapeHtml3(description);
		unescapedDescription = unescapedDescription.replace("<![CDATA[", "").replace("]]>", "");
		String cleanDescription = Jsoup.parse(Jsoup.clean(unescapedDescription, Whitelist.none())).text();
		// fix for Tomcat blog
		cleanDescription = cleanDescription.replace("~", "");
		ArrayList<String> links = pullLinks(cleanDescription);
		for (String link : links) {
			cleanDescription = cleanDescription.replace(link, "");
		}

		if (cleanDescription.length() >= 140) {
			cleanDescription = cleanDescription.substring(0, 140);
			cleanDescription += "...";
		}
		return cleanDescription.trim();
	}

	public ArrayList<String> pullLinks(String text) {
		ArrayList<String> links = new ArrayList<String>();

		String regex = "\\(?\\b(mailto:|ftp://|http://|https://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		while (m.find()) {
			String urlStr = m.group();
			if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
				urlStr = urlStr.substring(1, urlStr.length() - 1);
			}
			links.add(urlStr);
		}
		return links;
	}

	public void setItemRepository(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

}
