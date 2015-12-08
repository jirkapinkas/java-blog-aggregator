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
import java.util.Map;
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
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory.getLogger(RssService.class);

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

	public List<Item> getItems(String location, int blogId, Map<String, Object> allLinksMap) throws RssException {
		return getItems(location, false, blogId, allLinksMap);
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
	private String stripNonValidCharacters(String in) {
		StringBuilder out = new StringBuilder(); // Used to hold the output.
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
		String outString = fixDate(out.toString());
		return outString.replaceAll("\\s", " ").replace("‘", "'").replace("’", "'").replace("“", "\"").replace("”", "\"").replace("–", "-").replace("‼", "-").replace("&ndash;", "-");
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
		Builder requestConfigBuilder = RequestConfig.custom().setSocketTimeout(100000).setConnectTimeout(100000).setCookieSpec(CookieSpecs.IGNORE_COOKIES);
		get.setConfig(requestConfigBuilder.build());
		get.setHeader("Accept", "application/xml,application/rss+xml,text/html,*/*");
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36");
		return get;
	}

	public List<Item> getItems(String location, boolean localFile, int blogId, Map<String, Object> allLinksMap) throws RssException {
		Node node = null;
		String page = null;

		try {
			Document document = null;
			if (localFile) {
				File file = new File(location);
				page = FileUtils.readFileToString(file).trim();
			} else {
				HttpGet get = constructGet(location);
				CloseableHttpResponse response = null;
				try {
					response = httpClient.execute(get);
					HttpEntity entity = response.getEntity();
					page = EntityUtils.toString(entity, "UTF-8").trim();
				} finally {
					if (response != null) {
						response.close();
					}
				}
			}
			page = stripNonValidCharacters(page);
			document = db.parse(new ByteArrayInputStream(page.getBytes(Charset.forName("UTF-8"))));
			node = document.getDocumentElement();
		} catch (Exception ex) {
			log.warn("error parsing XML file: " + location);
			throw new RssException(ex.getMessage());
		}

		if ("rss".equals(node.getNodeName())) {
			return getRssItems(new StringReader(page), blogId, allLinksMap);
		} else if ("feed".equals(node.getNodeName())) {
			return getAtomItems(new StringReader(page), blogId, allLinksMap);
		} else {
			throw new RssException("unknown RSS type: " + location);
		}
	}

	protected String getRealLink(String link, HttpClientContext context) throws UrlException {
		link = link.trim();
		String realLink = null;
		try {
			HttpGet get = constructGet(link);
			CloseableHttpResponse response = null;
			try {
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
			log.debug("Stacktrace", e);
			log.error("Exception downloading real link: " + link);
			throw new UrlException("Exception during downloading: " + link);
		}
		if(realLink != null) {
			// fixes for stupid blogs
			realLink = realLink.replace("?utm_campaign=infoq_content&utm_source=infoq&utm_medium=feed&utm_term=Microservices", "");
			realLink = realLink.replace("?utm_campaign=infoq_content&utm_source=infoq&utm_medium=feed&utm_term=Java", "");
			realLink = realLink.replace("?utm_source=rss&utm_medium=rss&utm_campaign=rss", "");
			realLink = realLink.trim();
		}
		return realLink;
	}

	private List<Item> getRssItems(Reader reader, int blogId, Map<String, Object> allLinksMap) throws RssException {
		ArrayList<Item> list = new ArrayList<Item>();
		try {
			TRss rss = (TRss) unmarshallerRss.unmarshal(reader);

			List<TRssChannel> channels = rss.getChannels();

			for (TRssChannel channel : channels) {
				List<TRssItem> items = channel.getItems();
				if (items != null) {
					for (TRssItem rssItem : items) {
						Item item = new Item();
						item.setTitle(cleanTitle(rssItem.getTitle()));
						if (rssItem.getDescription() != null) {
							item.setDescription(cleanDescription(rssItem.getDescription().trim()));
						} else if (rssItem.getEncoded() != null) {
							item.setDescription(cleanDescription(rssItem.getEncoded().trim()));
						} else {
							throw new UnsupportedOperationException("unknown description");
						}
						Date pubDate = getRssDate(rssItem.getPubDate());
						item.setPublishedDate(pubDate);
						String link = null;
						if (rssItem.getOrigLink() != null) {
							link = rssItem.getOrigLink();
						} else {
							link = rssItem.getLink();
						}
						if (allLinksMap.containsKey(link)) {
							// skip this item, it's already in the database
							continue;
						}
						try {
							item.setLink(getRealLink(link, HttpClientContext.create()));
						} catch (UrlException e) {
							item.setError(e.getMessage());
						}
						list.add(item);
					}
				}
			}
		} catch (JAXBException | ParseException e) {
			throw new RssException(e);
		}
		return list;
	}

	private List<Item> getAtomItems(Reader reader, int blogId, Map<String, Object> allLinksMap) throws RssException {
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
				if (description == null) {
					throw new UnsupportedOperationException("unknown description");
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
				if (allLinksMap.containsKey(link)) {
					// skip this item, it's already in the database
					continue;
				}
				try {
					item.setLink(getRealLink(link, HttpClientContext.create()));
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
				try {
					return new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(stringDate);
				} catch (ParseException e3) {
					try {
						return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(stringDate);
					} catch (ParseException e4) {
						return new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).parse(stringDate);
					}
				}
			}
		}
	}

	private String cleanXml10(String xml) {
		String xml10pattern = "[^" + "\u0009\r\n" + "\u0020-\uD7FF" + "\uE000-\uFFFD" + "\ud800\udc00-\udbff\udfff" + "]";
		return xml.replaceAll(xml10pattern, "");
	}

	public String cleanTitle(String title) {
		String textTitle = Jsoup.parse(title).text();
		textTitle = textTitle.replace("[OmniFaces utilities 2.0]", "").trim();
		return cleanXml10(textTitle);
	}

	public String cleanDescription(String description) {
		String unescapedDescription = StringEscapeUtils.unescapeHtml3(description);
		unescapedDescription = unescapedDescription.replace("<![CDATA[", "").replace("]]>", "");
		unescapedDescription = unescapedDescription.replace("<br />", "BREAK_HERE").replace("<br/>", "BREAK_HERE").replace("<br>", "BREAK_HERE").replace("&lt;br /&gt;", "BREAK_HERE")
				.replace("&lt;br/&gt;", "BREAK_HERE").replace("&lt;br&gt;", "BREAK_HERE");
		String cleanDescription = Jsoup.parse(Jsoup.clean(unescapedDescription, Whitelist.none())).text();
		cleanDescription = cleanDescription.replace("BREAK_HERE", " ");
		// fix for Tomcat blog
		cleanDescription = cleanDescription.replace("~", "");
		cleanDescription = cleanDescription.replace("... Continue reading", "");
		cleanDescription = cleanDescription.replace("[OmniFaces utilities]", "");
		cleanDescription = cleanDescription.replace("[Note from Pinal]:", "");
		cleanDescription = cleanDescription.replace("[Additional]", "");
		ArrayList<String> links = pullLinks(cleanDescription);
		for (String link : links) {
			cleanDescription = cleanDescription.replace(link, "");
		}

		// split words which are more than 25 characters long
		StringBuilder finalDescription = new StringBuilder(cleanDescription.length());
		int lastSpace = 0;
		for (int i = 0; i < cleanDescription.length(); i++) {
			finalDescription.append(cleanDescription.charAt(i));
			if (cleanDescription.charAt(i) == ' ') {
				lastSpace = 0;
			}
			if (lastSpace == 25) {
				lastSpace = 0;
				finalDescription.append(" ");
			}
			lastSpace++;
		}

		// return only first 140 characters (plus '...')
		String returnDescription = finalDescription.toString();
		// this will replace all multiple whitespaces with just single
		// whitespace
		// fix for http://www.tutorial4soft.com/feeds/posts/default?alt=rss
		returnDescription = returnDescription.replaceAll("[^\\x00-\\x7F]", " ").trim();
		returnDescription = returnDescription.trim().replaceAll("\\s+", " ").trim();
		if (returnDescription.length() >= 140) {
			returnDescription = returnDescription.substring(0, 140);
			returnDescription += "...";
		}
		return returnDescription.trim();
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
