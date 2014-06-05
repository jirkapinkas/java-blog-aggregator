package cz.jiripinkas.jba.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

	List<Item> findByBlog(Blog blog, Pageable pageable);

	Item findByBlogAndLink(Blog blog, String link);

	@Query("select i from Item i join fetch i.blog where i.publishedDate >= ?1")
	List<Item> findPageAllItems(Date publishedDate, Pageable pageable);

	@Query("select i from Item i join fetch i.blog where i.enabled = true and i.publishedDate >= ?1")
	List<Item> findPageEnabled(Date publishedDate, Pageable pageable);
}
