package cz.jiripinkas.jba.dto;

import java.util.Date;

public class ItemDto {

	private int id;

	private boolean enabled;

	private String title;

	private String description;

	private String link;

	private Date publishedDate;

	private BlogDto blog;

	private int clickCount;

	private int likeCount;

	private int dislikeCount;

	private int twitterRetweetCount;

	private int facebookShareCount;

	private int linkedinShareCount;

	private int displayLikeCount;

	public void setDisplayLikeCount(int displayLikeCount) {
		this.displayLikeCount = displayLikeCount;
	}

	public int getDisplayLikeCount() {
		return displayLikeCount;
	}

	public int getTwitterRetweetCount() {
		return twitterRetweetCount;
	}

	public void setTwitterRetweetCount(int twitterRetweetCount) {
		this.twitterRetweetCount = twitterRetweetCount;
	}

	public int getFacebookShareCount() {
		return facebookShareCount;
	}

	public void setFacebookShareCount(int facebookShareCount) {
		this.facebookShareCount = facebookShareCount;
	}

	public int getLinkedinShareCount() {
		return linkedinShareCount;
	}

	public void setLinkedinShareCount(int linkedinShareCount) {
		this.linkedinShareCount = linkedinShareCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getDislikeCount() {
		return dislikeCount;
	}

	public void setDislikeCount(int dislikeCount) {
		this.dislikeCount = dislikeCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public int getClickCount() {
		return clickCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public BlogDto getBlog() {
		return blog;
	}

	public void setBlog(BlogDto blog) {
		this.blog = blog;
	}

}
