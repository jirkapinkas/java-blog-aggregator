package cz.jiripinkas.jba.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Category {

	@Id
	@GeneratedValue
	private int id;

	private String name;

	@Column(name = "short_name")
	private String shortName;

	@OneToMany(mappedBy = "category")
	private List<Blog> blogs;

	@Transient
	private int blogCount;

	public int getBlogCount() {
		return blogCount;
	}

	public void setBlogCount(int blogCount) {
		this.blogCount = blogCount;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setBlogs(List<Blog> blogs) {
		this.blogs = blogs;
	}

	public List<Blog> getBlogs() {
		return blogs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
