package cz.jiripinkas.jba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cz.jiripinkas.jba.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Category findByShortName(String shortName);

}
