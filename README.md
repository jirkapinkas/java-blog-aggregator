<h1>Java Blog Aggregator</h1>

<p>Project <a href="http://www.topjavablogs.com" target="_blank" title="Top Java Blogs">Top Java Blogs</a> uses this system to aggregate latest news from Java world.</p>

<p><strong>NOTE: On January 16 2016 the project has moved to new repository: <a href="https://github.com/jirkapinkas/java-blog-aggregator-boot">java-blog-aggregator-boot</a> and now runs on Spring Boot and instead of JSP is used Thymeleaf.</strong></p>

<h2>To run in development mode (using embedded HSQL database):</h2>

<p>
Run this application using embedded Jetty server: <code>mvn -P dev jetty:run -Dspring.profiles.active="dev"</code>
</p>

<p>This will start embedded Jetty server on port 8080 and you can access your application here: <code>http://localhost:8080</code></p>

<h2>To deploy on Heroku:</h2>

<p>
first run: <code>mvn package</code><br />
next goto target directory and run: <code>heroku deploy:war --war jba.war --app YOUR_APP</code><br />
To deploy WAR to Heroku see this article: <a href="https://devcenter.heroku.com/articles/war-deployment" target="_blank">https://devcenter.heroku.com/articles/war-deployment</a><br />

You can use this user to login (name / password): <code>admin / admin</code>
</p>

<h2>To develop JBA from scratch:</h2>

<p>
Because I recorded this project creation from scratch, you can learn from it:
</p>

<ul>
	<li>
		<a href="https://www.youtube.com/playlist?list=PLmcxdcWPhFqMq2BctGktOcIJKUw23wJeh" target="_blank" title="Java Blog Aggregator on YouTube">YouTube playlist</a>
	</li>
    <li>
		<a href="http://www.javavids.com/tutorial/spring-web-application-tutorial-java-blogs-aggregator.html" target="_blank" title="Java Blog Aggregator on JavaVids">On JavaVids.com</a> (with code snippets)
	</li>
</ul>

<h2>Related projects:</h2>

<ul>
	<li>
		<a href="http://www.javavids.com" target="_blank" title="Java video tutorials">Java video tutorials</a> (free online tutorials)
	</li>
    <li>
		<a href="http://www.java-skoleni.cz" target="_blank" title="Java školení">Java školení</a> (in Czech)
	</li>
</ul>