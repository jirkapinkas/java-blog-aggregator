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
