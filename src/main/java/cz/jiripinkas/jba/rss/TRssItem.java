package cz.jiripinkas.jba.rss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class TRssItem {

	private String title;

	private String description;

	private String pubDate;

	private String link;

	@XmlElement(namespace = "http://rssnamespace.org/feedburner/ext/1.0")
	private String origLink;

	@XmlElement(namespace = "http://purl.org/rss/1.0/modules/content/")
	private String encoded;

	public String getEncoded() {
		return encoded;
	}

	public void setEncoded(String encoded) {
		this.encoded = encoded;
	}

	public String getOrigLink() {
		return origLink;
	}

	public void setOrigLink(String origLink) {
		this.origLink = origLink;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
