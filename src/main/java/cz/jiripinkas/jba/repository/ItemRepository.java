package cz.jiripinkas.jba.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

	List<Item> findByBlog(Blog blog, Pageable pageable);

	Item findByBlogAndLink(Blog blog, String link);

	@Query("select i from Item i join fetch i.blog where i.publishedDate >= ?1")
	List<Item> findPageAllItems(Date publishedDate, Pageable pageable);

	@Query("select i from Item i join fetch i.blog where i.enabled = true and i.publishedDate >= ?1")
	List<Item> findPageEnabled(Date publishedDate, Pageable pageable);

	@Query("select i from Item i join fetch i.blog where i.enabled = true and i.blog.shortName = ?1")
	List<Item> findBlogPageEnabled(String shortName, Pageable pageable);

	@Transactional
	@Modifying
	@Query("update Item i set i.likeCount = i.likeCount + ?2 where i.id = ?1")
	void changeLike(int id, int amount);

	@Query("select new map(i.likeCount as like, i.clickCount as click) from Item i where i.id = ?1")
	Map<String, Integer> getLikeAndClickCount(int id);

	@Transactional
	@Modifying
	@Query("update Item i set i.dislikeCount = i.dislikeCount + ?2 where i.id = ?1")
	void changeDislike(int id, int amount);

	@Query("select i.dislikeCount from Item i where i.id = ?1")
	int getDislikeCount(int id);

	@Transactional
	@Modifying
	@Query("update Item i set i.clickCount = i.clickCount + 1 where i.id = ?1")
	void incClickCount(int id);

	@Query("select i.clickCount from Item i where i.id = ?1")
	int getClickCount(int id);

}
