package cz.jiripinkas.jba.atom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Link {

	@XmlAttribute
	private String href;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}
