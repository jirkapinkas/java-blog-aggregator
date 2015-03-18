package cz.jiripinkas.jba.ddos;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class DDOSProtectionFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String fwd = ((HttpServletRequest)request).getHeader("X-Forwarded-For");
		String method = ((HttpServletRequest)request).getMethod();
		
		HashMap<String, String> blockedIps = new HashMap<String, String>();
		blockedIps.put("58.110.119.51", null);
		blockedIps.put("68.56.43.151", null);
		blockedIps.put("175.45.19.158", null);
		blockedIps.put("49.242.22.226", null);
		blockedIps.put("74.86.158.106", null);
		blockedIps.put("110.142.179.19", null);
		blockedIps.put("5.108.107.148", null);
		blockedIps.put("94.200.219.30", null);
		blockedIps.put("58.110.117.134", null);
		blockedIps.put("91.250.87.195", null);
		blockedIps.put("74.6.254.126", null);
		blockedIps.put("23.29.122.195", null);
		blockedIps.put("216.46.175.37", null);
		blockedIps.put("10.223.63.94", null);
		blockedIps.put("50.18.102.132", null);
		
		if(fwd != null && blockedIps.containsKey(fwd) && method.equalsIgnoreCase("HEAD")) {
			System.out.println("BLOCKED IP ADDRESS: " + fwd);
			((HttpServletResponse)response).sendError(666);
			return;
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
