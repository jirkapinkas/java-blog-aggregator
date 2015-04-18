package cz.jiripinkas.jba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cz.jiripinkas.jba.entity.NewsItem;

public interface NewsItemRepository extends JpaRepository<NewsItem, Integer> {

	NewsItem findByShortName(String shortName);

}
