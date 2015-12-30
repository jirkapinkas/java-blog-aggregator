package cz.jiripinkas.jba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cz.jiripinkas.jba.entity.Blog;

public interface BlogRepository extends JpaRepository<Blog, Integer> {

	@Query("select distinct b from Blog b left join fetch b.items where b.user.id = ?1 order by b.id")
	List<Blog> findByUserId(int id);

	Blog findByUrl(String url);

	@Query("select b from Blog b join fetch b.user where b.id = ?1")
	Blog findOneFetchUser(int id);

	@Query("select b from Blog b left join fetch b.category where b.shortName = ?1")
	Blog findByShortName(String shortName);

	@Query("select b from Blog b join fetch b.user left join fetch b.category order by b.shortName")
	List<Blog> findAllFetchUser();

	@Query("select b from Blog b join fetch b.user join fetch b.category order by b.shortName")
	List<Blog> findAllWithCategoryFetchUser();

	Blog findByIdAndUserName(int id, String username);

	@Modifying
	@Query("update Blog b set b.lastCheckErrorText = null, b.lastCheckErrorCount = 0, b.lastCheckStatus = true where b.id = ?1")
	void saveOk(int blogId);

	@Modifying
	@Query("update Blog b set b.lastCheckErrorText = ?3, b.lastCheckErrorCount = ?2, b.lastCheckStatus = false where b.id = ?1")
	void saveFail(int blogId, int errorCount, String errorText);

	int countByCategoryId(int categoryId);

	@Modifying
	@Query("update Blog b set b.lastIndexedDate = current_timestamp where b.id = ?1")
	void saveLastIndexedDate(int blogId);

	@Modifying
	@Query("update Blog b set b.popularity = ?2 where b.id = ?1")
	void setPopularity(int blogId, int popularity);

	@Query("select count(b) from Blog b where b.category is null")
	long countUnapproved();

}
