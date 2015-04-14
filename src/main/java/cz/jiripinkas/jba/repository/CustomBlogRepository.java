package cz.jiripinkas.jba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cz.jiripinkas.jba.entity.CustomBlog;

public interface CustomBlogRepository extends JpaRepository<CustomBlog, Integer> {

	CustomBlog findByShortName(String shortName);

}
