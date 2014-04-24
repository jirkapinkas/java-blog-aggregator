package cz.jiripinkas.jba.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cz.jiripinkas.jba.entity.Item;
import cz.jiripinkas.jba.entity.Blog;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

	List<Item> findByBlog(Blog blog, Pageable pageable);
	
	Item findByBlogAndLink(Blog blog, String link);
	
	@Query("select i from Item i join fetch i.blog")
	List<Item> findPageAllItems(Pageable pageable);

	@Query("select i from Item i join fetch i.blog where i.enabled = true")
	List<Item> findPageEnabled(Pageable pageable);
}
