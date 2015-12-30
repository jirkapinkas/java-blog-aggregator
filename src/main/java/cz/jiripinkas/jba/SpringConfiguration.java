package cz.jiripinkas.jba;

import java.util.Arrays;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.dozer.DozerBeanMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@EnableTransactionManagement
@EnableJpaRepositories("cz.jiripinkas.jba.repository")
@ComponentScan
@Configuration
@EnableCaching
public class SpringConfiguration {

	@Bean
	public JpaTransactionManager transactionManager(DataSource dataSource, EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
		transactionManager.setDataSource(dataSource);
		return transactionManager;
	}

	@Bean
	public DozerBeanMapper dozerBeanMapper() {
		return new DozerBeanMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("icons"), new ConcurrentMapCache("configuration"),
				new ConcurrentMapCache("categories"), new ConcurrentMapCache("blogCount"),
				new ConcurrentMapCache("itemCount"), new ConcurrentMapCache("userCount"),
				new ConcurrentMapCache("blogCountUnapproved")));
		return cacheManager;
	}

}
