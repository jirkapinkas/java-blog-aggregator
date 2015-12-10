package cz.jiripinkas.jba.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
public class Item {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(length = 1000)
	private String title;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(length = Integer.MAX_VALUE)
	private String description;

	/**
	 * When was the item published by it's owner
	 */
	@Column(name = "published_date")
	private Date publishedDate;

	@Column(length = 1000)
	private String link;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blog_id")
	private Blog blog;

	private boolean enabled;

	@Column(name = "click_count", nullable = false)
	private Integer clickCount;

	@Column(name = "like_count", nullable = false)
	private Integer likeCount;

	@Column(name = "dislike_count", nullable = false)
	private Integer dislikeCount;

	@Column(name = "twitter_retweet_count", nullable = false)
	private Integer twitterRetweetCount;

	@Column(name = "facebook_share_count", nullable = false)
	private Integer facebookShareCount;

	@Column(name = "linkedin_share_count", nullable = false)
	private Integer linkedinShareCount;

	/**
	 * When was the item saved to local database
	 */
	@Column(name = "saved_date")
	private Date savedDate;

	@Transient
	private String error;

	public Item() {
		setEnabled(true);
		setClickCount(0);
		setLikeCount(0);
		setDislikeCount(0);
		setTwitterRetweetCount(0);
		setFacebookShareCount(0);
		setLinkedinShareCount(0);
	}

	public Integer getTwitterRetweetCount() {
		return twitterRetweetCount;
	}

	public void setTwitterRetweetCount(Integer twitterRetweetCount) {
		this.twitterRetweetCount = twitterRetweetCount;
	}

	public Integer getFacebookShareCount() {
		return facebookShareCount;
	}

	public void setFacebookShareCount(Integer facebookShareCount) {
		this.facebookShareCount = facebookShareCount;
	}

	public Integer getLinkedinShareCount() {
		return linkedinShareCount;
	}

	public void setLinkedinShareCount(Integer linkedinShareCount) {
		this.linkedinShareCount = linkedinShareCount;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public Integer getDislikeCount() {
		return dislikeCount;
	}

	public void setDislikeCount(Integer dislikeCount) {
		this.dislikeCount = dislikeCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	public Integer getClickCount() {
		return clickCount;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Date getSavedDate() {
		return savedDate;
	}

	public void setSavedDate(Date savedDate) {
		this.savedDate = savedDate;
	}

}
