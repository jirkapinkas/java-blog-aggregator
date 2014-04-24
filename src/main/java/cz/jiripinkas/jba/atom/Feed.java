package cz.jiripinkas.jba.atom;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Feed {

	@XmlElement(name = "entry")
	private List<Entry> entries;

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
}
