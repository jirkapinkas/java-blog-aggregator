package cz.jiripinkas.jba.rss;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class TRssChannel {

	@XmlElement(name = "item")
	List<TRssItem> items;

	public List<TRssItem> getItems() {
		return items;
	}

	public void setItems(List<TRssItem> items) {
		this.items = items;
	}

}
