package cz.jiripinkas.jba.atom;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
public class Entry {

	private String title;

	@XmlElement(name = "link")
	private List<Link> links;

	@XmlElement(namespace = "http://rssnamespace.org/feedburner/ext/1.0")
	private String origLink;

	private XMLGregorianCalendar updated;

	private XMLGregorianCalendar published;

	private String content;

	private String summary;

	public String getOrigLink() {
		return origLink;
	}

	public void setOrigLink(String origLink) {
		this.origLink = origLink;
	}

	public XMLGregorianCalendar getPublished() {
		return published;
	}

	public void setPublished(XMLGregorianCalendar published) {
		this.published = published;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public String getContent() {
		return content;
	}

	public XMLGregorianCalendar getUpdated() {
		return updated;
	}

	public void setUpdated(XMLGregorianCalendar updated) {
		this.updated = updated;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
