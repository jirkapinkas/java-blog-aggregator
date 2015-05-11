<h1>Java Blog Aggregator</h1>

<p>Project <a href="http://www.topjavablogs.com" target="_blank" title="Top Java Blogs">Top Java Blogs</a> uses this system to aggregate latest news from Java world.</p>

<h2>Development (with embedded HSQL database):</h2>

<p>
Run this application using embedded Jetty server: <code>mvn -P dev jetty:run -Dspring.profiles.active="dev"</code>
</p>

<p>This will start embedded Jetty server on port 8080 and you can access your application here: <code>http://localhost:8080</code><br />
You can use this user to login (name / password): <code>admin / admin</code>
</p>

<h2>Production</h2>

<p>You can easily add this application either to Heroku or OpenShift. It uses PostgreSQL database.</p>

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
		<a href="http://www.java-skoleni.cz" target="_blank" title="Java skoleni">Java skoleni</a> (in Czech)
	</li>
</ul>