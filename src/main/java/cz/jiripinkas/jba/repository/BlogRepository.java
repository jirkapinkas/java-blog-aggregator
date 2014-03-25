package cz.jiripinkas.jba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cz.jiripinkas.jba.entity.Blog;

public interface BlogRepository extends JpaRepository<Blog, Integer>{

}
