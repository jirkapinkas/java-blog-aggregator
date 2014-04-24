package cz.jiripinkas.jba.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import cz.jiripinkas.jba.entity.Blog;
import cz.jiripinkas.jba.entity.User;
import cz.jiripinkas.jba.repository.BlogRepository;
import cz.jiripinkas.jba.repository.ItemRepository;
import cz.jiripinkas.jba.repository.RoleRepository;
import cz.jiripinkas.jba.repository.UserRepository;

@Transactional
@Service
@Profile("dev")
@DependsOn("initDbService")
public class InitDbTestService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private ItemRepository itemRepository;

//	@PostConstruct
	public void init() {
		saveBlog("http://henrikwarne.com/feed/");
		saveBlog("http://antoniogoncalves.org/feed/");
		saveBlog("http://feeds.feedburner.com/adam-bien/recent-rss?format=xml");
		saveBlog("http://feeds.feedburner.com/juristrumpflohner?format=xml");
		saveBlog("http://javatemple.blogspot.com/feeds/posts/default?alt=rss");
		saveBlog("http://feeds.feedburner.com/HelperCode?format=xml");
		saveBlog("http://invariantproperties.com/feed/");
		saveBlog("http://feeds.feedburner.com/codinghorror?format=xml");
		saveBlog("http://insightfullogic.com/blog/rss");
		saveBlog("http://techblog.bozho.net/?feed=rss2");
		saveBlog("http://martinfowler.com/feed.atom");
		saveBlog("http://javax0.wordpress.com/feed/");
		saveBlog("http://www.benmccann.com/blog/feed/");
		saveBlog("http://feeds.feedburner.com/javaposse");
		saveBlog("http://www.takipiblog.com/feed/");
		saveBlog("https://plumbr.eu/feed");
		saveBlog("http://eclipsesource.com/blogs/author/irbull/feed/");
		saveBlog("http://www.javaworld.com/category/core-java/index.rss");
		saveBlog("http://www.theserverside.com/rss/forum.tss?forum_id=2");
		saveBlog("http://in.relation.to/service/Feed/atom/Area/Bloggers/Comments/exclude");
		saveBlog("http://www.tomcatexpert.com/blog/feed");
		saveBlog("https://spring.io/blog.atom");
		saveBlog("http://feeds.feedburner.com/FeedForMkyong?format=xml");
		saveBlog("http://vaadin.com/blog/-/blogs/rss");
		saveBlog("http://balusc.blogspot.com/feeds/posts/default");
		saveBlog("http://feeds.feedburner.com/Javarevisited?format=xml");
		saveBlog("http://feeds.feedburner.com/InspiredByActualEvents");
		saveBlog("http://blog.jooq.org/feed/");
		saveBlog("http://feeds.feedblitz.com/PetriKainulainen");
		saveBlog("http://feeds.feedburner.com/MilesToGo");
		saveBlog("http://mechanical-sympathy.blogspot.com/feeds/posts/default?alt=rss");
		saveBlog("http://olivergierke.de/atom.xml");
		saveBlog("http://blog.frankel.ch/feed");
		saveBlog("http://vladmihalcea.com/feed/");
		saveBlog("http://blog.lckymn.com/feed/");
		saveBlog("http://java-performance.info/feed/");
		saveBlog("http://steveonjava.com/feed/");
		saveBlog("https://raibledesigns.com/rd/feed/entries/rss");
		saveBlog("http://vanillajava.blogspot.com/feeds/posts/default?alt=rss");
		saveBlog("http://www.beabetterdeveloper.com/feeds/posts/default?alt=rss");
		saveBlog("http://feeds.feedburner.com/JavaAdventCalendar");
		saveBlog("http://feeds.feedburner.com/JavaEESupportPatterns");
		saveBlog("http://feeds.feedburner.com/baeldung");
		saveBlog("http://blog.bdoughan.com/feeds/posts/default");
		saveBlog("http://zeroturnaround.com/rebellabs/feed/");
	}
	
	private void saveBlog(String url) {
		User user = userRepository.findByName("admin");
		Blog blog = new Blog(url, "test", user);
		blogRepository.save(blog);
	}
}
