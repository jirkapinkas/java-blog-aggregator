package cz.jiripinkas.jba.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cz.jiripinkas.jba.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

	@Query("select i.id from Item i where i.link = ?1 and i.blog.id = ?2")
	Integer findItemIdByLinkAndBlogId(String link, int blogId);

	@Query("select i from Item i join fetch i.blog b left join fetch b.category cat where i.enabled = true and cat.shortName = ?1")
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

	@Transactional
	@Modifying
	@Query("update Item i set i.savedDate = i.publishedDate where i.savedDate is null")
	void updateSavedDates();

	@Query("select sum(coalesce(i.likeCount, 0) + ((log(coalesce(i.clickCount, 0) + 1) * 10) + (log(coalesce(i.twitterRetweetCount, 0) + 1) * 10) + (log(coalesce(i.facebookShareCount, 0) + 1) * 3) + (log(coalesce(i.linkedinShareCount, 0) + 1) * 10))) / coalesce(count(i), 1) from Item i where i.blog.id = ?1 and i.savedDate > ?2")
	Integer getSocialSum(int blogId, Date dateFrom);

}
