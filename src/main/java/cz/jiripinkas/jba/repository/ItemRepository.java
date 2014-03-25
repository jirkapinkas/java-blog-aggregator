package cz.jiripinkas.jba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cz.jiripinkas.jba.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{

}
