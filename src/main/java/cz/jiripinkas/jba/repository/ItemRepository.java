package cz.jiripinkas.jba.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cz.jiripinkas.jba.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

	@Query("select i.id from Item i where i.link = ?1 and i.blog.id = ?2")
	Integer findItemIdByLinkAndBlogId(String link, int blogId);

	@Query("select i from Item i join fetch i.blog left join fetch i.blog.category cat where i.publishedDate >= ?1 and cat.id IN ?2")
	List<Item> findPageAllItemsInCategory(Date publishedDate, List<Integer> selectedCategories, Pageable pageable);

	@Query("select i from Item i join fetch i.blog left join fetch i.blog.category cat where i.enabled = true and i.publishedDate >= ?1 and cat.id IN ?2")
	List<Item> findPageEnabledInCategory(Date publishedDate, List<Integer> selectedCategories, Pageable pageable);

	@Query("select i from Item i join fetch i.blog left join fetch i.blog.category where i.enabled = true and i.blog.shortName = ?1")
	List<Item> findBlogPageEnabled(String shortName, Pageable pageable);

	@Query("select i from Item i join fetch i.blog left join fetch i.blog.category cat where i.enabled = true and cat.shortName = ?1")
	List<Item> findCategoryPageEnabled(String shortName, Pageable pageable);

	@Transactional
	@Modifying
	@Query("update Item i set i.likeCount = i.likeCount + ?2 where i.id = ?1")
	void changeLike(int id, int amount);

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

	@Query("select i.link from Item i")
	List<String> findAllLinks();

	@Query("select lower(i.title) from Item i")
	List<String> findAllLowercaseTitles();

	@Transactional
	@Modifying
	@Query("update Item i set i.twitterRetweetCount = ?2 where i.id = ?1")
	void setTwitterRetweetCount(int id, int count);

	@Transactional
	@Modifying
	@Query("update Item i set i.facebookShareCount = ?2 where i.id = ?1")
	void setFacebookShareCount(int id, int shares);

	@Transactional
	@Modifying
	@Query("update Item i set i.linkedinShareCount = ?2 where i.id = ?1")
	void setLinkedinShareCount(int id, int count);


}
