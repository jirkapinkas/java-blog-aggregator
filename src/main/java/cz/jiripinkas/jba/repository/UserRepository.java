package cz.jiripinkas.jba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cz.jiripinkas.jba.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByName(String name);


}
