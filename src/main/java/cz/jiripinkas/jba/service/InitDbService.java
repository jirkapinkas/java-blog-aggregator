package cz.jiripinkas.jba.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.entity.Category;
import cz.jiripinkas.jba.entity.Configuration;
import cz.jiripinkas.jba.entity.Role;
import cz.jiripinkas.jba.entity.User;
import cz.jiripinkas.jba.repository.BlogRepository;
import cz.jiripinkas.jba.repository.CategoryRepository;
import cz.jiripinkas.jba.repository.ItemRepository;
import cz.jiripinkas.jba.repository.RoleRepository;
import cz.jiripinkas.jba.repository.UserRepository;

@Transactional
@Service
public class InitDbService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@PostConstruct
	public void init() {
		if (roleRepository.findByName("ROLE_ADMIN") == null) {
			Role roleUser = new Role();
			roleUser.setName("ROLE_USER");
			roleRepository.save(roleUser);

			Role roleAdmin = new Role();
			roleAdmin.setName("ROLE_ADMIN");
			roleRepository.save(roleAdmin);

			User userAdmin = new User();
			userAdmin.setEnabled(true);
			userAdmin.setAdmin(true);
			userAdmin.setName("admin");
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			userAdmin.setPassword(encoder.encode("admin"));
			List<Role> roles = new ArrayList<Role>();
			roles.add(roleAdmin);
			roles.add(roleUser);
			userAdmin.setRoles(roles);
			userRepository.save(userAdmin);

			Category springCategory = new Category();
			springCategory.setName("Spring");
			springCategory.setShortName("spring");
			springCategory = categoryRepository.save(springCategory);
			

			Blog blogSpring = new Blog();
			blogSpring.setName("Spring");
			blogSpring.setUrl("http://spring.io/blog.atom");
			blogSpring.setHomepageUrl("http://spring.io/");
			blogSpring.setShortName("spring");
			blogSpring.setUser(userAdmin);
			blogSpring.setCategory(springCategory);
			blogRepository.save(blogSpring);

			Blog blogJavavids = new Blog();
			blogJavavids.setName("javavids");
			blogJavavids.setUrl("http://feeds.feedburner.com/javavids?format=xml");
			blogJavavids.setHomepageUrl("http://www.javavids.com");
			blogJavavids.setShortName("javavids");
			blogJavavids.setUser(userAdmin);
			blogRepository.save(blogJavavids);

			Category czechTrainingsCategory = new Category();
			czechTrainingsCategory.setName("Czech Trainings");
			czechTrainingsCategory.setShortName("czech-trainings");
			czechTrainingsCategory = categoryRepository.save(czechTrainingsCategory);
			
			Blog blogJavaSkoleni = new Blog();
			blogJavaSkoleni.setName("java skoleni");
			blogJavaSkoleni.setUrl("http://novinky.seico.cz/java-skoleni");
			blogJavaSkoleni.setHomepageUrl("http://www.java-skoleni.cz");
			blogJavaSkoleni.setShortName("java-skoleni");
			blogJavaSkoleni.setUser(userAdmin);
			blogJavaSkoleni.setCategory(czechTrainingsCategory);
			blogRepository.save(blogJavaSkoleni);

			Blog blogSqlSkoleni = new Blog();
			blogSqlSkoleni.setName("sql skoleni");
			blogSqlSkoleni.setUrl("http://novinky.seico.cz/sql-skoleni");
			blogSqlSkoleni.setHomepageUrl("http://www.sql-skoleni.cz");
			blogSqlSkoleni.setShortName("sql-skoleni");
			blogSqlSkoleni.setUser(userAdmin);
			blogSqlSkoleni.setCategory(czechTrainingsCategory);
			blogRepository.save(blogSqlSkoleni);
			
			
		}

		Configuration configuration = configurationService.find();
		if(configuration == null) {
			configuration = new Configuration();
			configuration.setTitle("Java Blog Aggregator");
			configuration.setBrandName("top java blogs");
			configuration.setFooter(
					"&copy; Jiri Pinkas \n" + 
					" | this project on <a href='https://github.com/jirkapinkas/java-blog-aggregator' target='_blank'>GitHub</a>\n" +
					" | related: <a href='http://www.javavids.com' target='_blank'>JavaVids</a>\n" +
					" | <a href='http://www.java-skoleni.cz' target='_blank'>Java školení</a>\n" +
					" | monitored using: <a href='http://sitemonitoring.sourceforge.net/' target='_blank' title='free open source website monitoring software'>sitemonitoring</a>\n" +
					" <br />\n" +
					" <br />\n" +
					" Top Java Blogs is a Java blog aggregator (with English-written blogs only) focused on Java SE, Java EE, Spring Framework and Hibernate.\n"
					);
			configuration.setGoogleAnalytics(
					"<script>\n" +
					"  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n" +
					"  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n" +
					"  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n" +
					"  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');\n" +
					"  ga('create', 'UA-49851353-1', 'topjavablogs.com');\n" +
					"  ga('send', 'pageview');\n" +
					"</script>\n"
					);
			configuration.setGoogleAdsense(
					"<script async src='//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js'></script>\n" + 
					"<!-- top java blogs responsive -->\n" + 
					"<ins class='adsbygoogle'\n" + 
					"     style='display:block'\n" + 
					"     data-ad-client='ca-pub-7085637172523095'\n" + 
					"     data-ad-slot='5679428406'\n" + 
					"     data-ad-format='auto'></ins>\n" + 
					"<script>\n" + 
					"(adsbygoogle = window.adsbygoogle || []).push({});\n" + 
					"</script>\n"
					);
			configurationService.save(configuration);
		}
	}
}
