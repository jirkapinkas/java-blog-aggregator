package cz.jiripinkas.jba.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ConfigurationInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private UserService userService;

	@Autowired
	private ItemService itemService;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		/*
		 * Attributes will be available in every page.
		 */
		if (modelAndView != null) {
			modelAndView.getModelMap().addAttribute("configuration", configurationService.find());
			modelAndView.getModelMap().addAttribute("categories", categoryService.findAll());
			modelAndView.getModelMap().addAttribute("lastIndexDate", blogService.getLastIndexDateMinutes());
			modelAndView.getModelMap().addAttribute("blogCount", blogService.count());
			if (request.isUserInRole("ROLE_ADMIN")) {
				modelAndView.getModelMap().addAttribute("itemCount", itemService.count());
				modelAndView.getModelMap().addAttribute("userCount", userService.count());
			}
		}
	}
}