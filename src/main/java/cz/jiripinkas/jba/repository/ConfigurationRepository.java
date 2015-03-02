package cz.jiripinkas.jba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cz.jiripinkas.jba.entity.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

}
