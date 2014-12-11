package cz.jiripinkas.jba.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import cz.jiripinkas.jba.annotation.UniqueBlog;

@Entity
public class Blog {

	@Id
	@GeneratedValue
	private Integer id;

	@Lob
	@Column(length = Integer.MAX_VALUE)
	private byte[] icon;

	@UniqueBlog(message = "This blog already exists!")
	@Size(min = 1, message = "Invalid URL!")
	@URL(message = "Invalid URL!")
	@Column(length = 1000, unique = true)
	private String url;

	@Size(min = 1, message = "Name must be at least 1 character!")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE)
	private List<Item> items;

	public Blog() {
	}

	public Blog(String url, String name, User user) {
		this.url = url;
		this.name = name;
		this.user = user;
	}

	public byte[] getIcon() {
		return icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
