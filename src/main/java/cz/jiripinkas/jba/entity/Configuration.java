package cz.jiripinkas.jba.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Configuration {

	@Id
	@GeneratedValue
	private int id;

	private String title;

	@Column(name = "brand_name")
	private String brandName;

	@Column(length = Integer.MAX_VALUE)
	@Lob
	private String footer;

	@Column(name = "google_adsense", length = Integer.MAX_VALUE)
	@Lob
	private String googleAdsense;

	@Column(name = "google_analytics", length = Integer.MAX_VALUE)
	@Lob
	private String googleAnalytics;

	@Column(name = "homepage_heading")
	private String homepageHeading;

	@Column(name = "top_heading")
	private String topHeading;

	@Column(name = "channel_title")
	private String channelTitle;

	@Column(name = "channel_link")
	private String channelLink;

	@Column(name = "channel_description")
	private String channelDescription;

	@Column(name = "news_social_buttons", length = Integer.MAX_VALUE)
	@Lob
	private String newsSocialButtons;

	@Column(name = "disqus_code", length = Integer.MAX_VALUE)
	@Lob
	private String disqusCode;

	@Lob
	@Column(length = Integer.MAX_VALUE)
	private byte[] icon;

	@Lob
	@Column(length = Integer.MAX_VALUE)
	private byte[] favicon;

	@Lob
	@Column(length = Integer.MAX_VALUE, name = "apple_touch_icon")
	private byte[] appleTouchIcon;

	public byte[] getFavicon() {
		return favicon;
	}

	public void setFavicon(byte[] favicon) {
		this.favicon = favicon;
	}

	public byte[] getAppleTouchIcon() {
		return appleTouchIcon;
	}

	public void setAppleTouchIcon(byte[] appleTouchIcon) {
		this.appleTouchIcon = appleTouchIcon;
	}

	public void setDisqusCode(String disqusCode) {
		this.disqusCode = disqusCode;
	}

	public String getDisqusCode() {
		return disqusCode;
	}

	public String getNewsSocialButtons() {
		return newsSocialButtons;
	}

	public void setNewsSocialButtons(String newsSocialButtons) {
		this.newsSocialButtons = newsSocialButtons;
	}

	public String getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}

	public String getChannelLink() {
		return channelLink;
	}

	public void setChannelLink(String channelLink) {
		this.channelLink = channelLink;
	}

	public String getChannelDescription() {
		return channelDescription;
	}

	public void setChannelDescription(String channelDescription) {
		this.channelDescription = channelDescription;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	public byte[] getIcon() {
		return icon;
	}

	public void setHomepageHeading(String homepageHeading) {
		this.homepageHeading = homepageHeading;
	}

	public String getHomepageHeading() {
		return homepageHeading;
	}

	public void setTopHeading(String topHeading) {
		this.topHeading = topHeading;
	}

	public String getTopHeading() {
		return topHeading;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getGoogleAdsense() {
		return googleAdsense;
	}

	public void setGoogleAdsense(String googleAdsense) {
		this.googleAdsense = googleAdsense;
	}

	public String getGoogleAnalytics() {
		return googleAnalytics;
	}

	public void setGoogleAnalytics(String googleAnalytics) {
		this.googleAnalytics = googleAnalytics;
	}

}
