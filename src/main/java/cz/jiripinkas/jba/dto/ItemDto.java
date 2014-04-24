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
