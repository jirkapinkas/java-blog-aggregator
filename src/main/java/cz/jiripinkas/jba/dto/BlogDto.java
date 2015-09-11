package cz.jiripinkas.jba.dto;

import cz.jiripinkas.jba.util.MyUtil;

public class BlogDto {

	private String name;

	private String nick;

	private String shortName;

	private int id;

	private CategoryDto category;

	public String getPublicName() {
		return MyUtil.getPublicName(nick, name);
	}

	public CategoryDto getCategory() {
		return category;
	}

	public void setCategory(CategoryDto category) {
		this.category = category;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
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

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

}
