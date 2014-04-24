package cz.jiripinkas.jba.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import cz.jiripinkas.jba.atom.Entry;
import cz.jiripinkas.jba.atom.Feed;
import cz.jiripinkas.jba.entity.Item;
import cz.jiripinkas.jba.exception.RssException;
import cz.jiripinkas.jba.rss.TRss;
import cz.jiripinkas.jba.rss.TRssChannel;
import cz.jiripinkas.jba.rss.TRssItem;

@Service
public class RssService {

	private static Unmarshaller unmarshallerRss;
	private static Unmarshaller unmarshallerAtom;
	private static DocumentBuilder db;

	static {
		try {
			System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36");
			System.setProperty("com.sun.net.ssl.checkRevocation", "false");
			JAXBContext jaxbContextRss = JAXBContext.newInstance(cz.jiripinkas.jba.rss.ObjectFactory.class);
			unmarshallerRss = jaxbContextRss.createUnmarshaller();
			JAXBContext jaxbContextAtom = JAXBContext.newInstance(Feed.class, Entry.class);
			unmarshallerAtom = jaxbContextAtom.createUnmarshaller();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Item> getItems(File file) throws RssException {
		return getItems(new StreamSource(file));
	}

	public List<Item> getItems(String url) throws RssException {
		return getItems(new StreamSource(url));
	}

	private List<Item> getItems(StreamSource source) throws RssException {

		Node node = null;

		InputSource inputSource = new InputSource();
		try {
			inputSource.setByteStream(source.getInputStream());
			inputSource.setCharacterStream(source.getReader());
			inputSource.setSystemId(source.getSystemId());
			Document document = db.parse(inputSource);
			node = document.getDocumentElement();
		} catch (Exception ex) {
			System.out.println("error parsing XML file");
//			ex.printStackTrace();
			throw new RssException(ex);
		}

		if ("rss".equals(node.getNodeName())) {
			return getRssItems(node);
		} else if ("feed".equals(node.getNodeName())) {
			return getAtomItems(source); // TODO WHY IS node NOT WORKING? JUST source?
		} else {
			throw new RssException("unknown RSS type");
		}

	}

	private List<Item> getRssItems(Node node) throws RssException {

		ArrayList<Item> list = new ArrayList<Item>();
		try {
			JAXBElement<TRss> jaxbElement = unmarshallerRss.unmarshal(node, TRss.class);
			TRss rss = jaxbElement.getValue();

			List<TRssChannel> channels = rss.getChannel();

			for (TRssChannel channel : channels) {
				List<TRssItem> items = channel.getItem();
				for (TRssItem rssItem : items) {
					Item item = new Item();
					item.setTitle(cleanTitle(rssItem.getTitle()));
					item.setDescription(cleanDescription(rssItem.getDescription()));
					item.setLink(rssItem.getLink());
					Date pubDate = getRssDate(rssItem.getPubDate());
					item.setPublishedDate(pubDate);
					list.add(item);
				}
			}
		} catch (JAXBException e) {
			throw new RssException(e);
		} catch (ParseException e) {
			throw new RssException(e);
		}
		if (list.size() == 0) {
			throw new RssException("Not supported RSS feed or without items");
		}
		return list;
	}

	private List<Item> getAtomItems(Source source) throws RssException {
		ArrayList<Item> list = new ArrayList<Item>();
		try {
			JAXBElement<Feed> jaxbElement = unmarshallerAtom.unmarshal(source, Feed.class);
			Feed rss = jaxbElement.getValue();
			List<Entry> entries = rss.getEntries();
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
				item.setLink(entry.getLink().getHref());
				Date pubDate = entry.getUpdated().toGregorianCalendar().getTime();
				item.setPublishedDate(pubDate);
				list.add(item);
			}
		} catch (JAXBException e) {
			throw new RssException(e);
		}
		if (list.size() == 0) {
			throw new RssException("Not supported RSS feed or without items");
		}
		return list;
	}
	
	public Date getRssDate(String stringDate) throws ParseException {
		try {
			return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(stringDate);
		} catch(ParseException e) {
			return new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH).parse(stringDate);
		}
	}

	public String cleanTitle(String title) {
		return Jsoup.parse(title).text();
	}

	// TODO TEST THIS
	public String cleanDescription(String description) {
		String cleanDescription = Jsoup.parse(description).text();
		cleanDescription = cleanDescription.replace("~", ""); // fix for Tomcat
																// blog
		ArrayList<String> links = pullLinks(cleanDescription);
		for (String link : links) {
			cleanDescription = cleanDescription.replace(link, "");
		}

		if (cleanDescription.length() >= 140) {
			cleanDescription = cleanDescription.substring(0, 140);
			cleanDescription += "...";
		}
		return cleanDescription;
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

}
