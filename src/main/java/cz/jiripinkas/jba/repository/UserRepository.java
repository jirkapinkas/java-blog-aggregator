package cz.jiripinkas.jba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cz.jiripinkas.jba.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByName(String name);

	@Query("select u from User u where u.admin = 1")
	User findAdmin();

	@Modifying
	@Query("update User u set u.password = ?1 where u.admin = 1")
	void updateAdminPassword(String password);

	@Modifying
	@Query("update User u set u.name = ?1 where u.admin = 1")
	void updateAdminName(String name);

}
