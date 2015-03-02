package cz.jiripinkas.jba.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ConfigurationInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			modelAndView.getModelMap().addAttribute("configuration", configurationService.find());
		}
	}
}