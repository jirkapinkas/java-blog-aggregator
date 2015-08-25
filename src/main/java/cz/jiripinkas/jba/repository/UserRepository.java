package cz.jiripinkas.jba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cz.jiripinkas.jba.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByName(String name);

	@Query("select u from User u where u.admin = true")
	User findAdmin();

	@Modifying
	@Query("update User u set u.password = ?1 where u.admin = true")
	void updateAdminPassword(String password);

	@Modifying
	@Query("update User u set u.name = ?1 where u.admin = true")
	void updateAdminName(String name);

	@Query("select distinct u from User u left join fetch u.roles")
	List<User> findAllFetchRoles();

}
