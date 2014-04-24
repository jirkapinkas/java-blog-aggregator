package cz.jiripinkas.jba.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.jiripinkas.jba.dto.ItemDto;
import cz.jiripinkas.jba.service.BlogService;
import cz.jiripinkas.jba.service.ItemService;
import cz.jiripinkas.jba.service.UserService;

@Controller
public class IndexController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BlogService blogService;

	@RequestMapping("/index")
	public String index(Model model, HttpServletRequest request) {
		boolean showAll = false;
		if (request.isUserInRole("ROLE_ADMIN")) {
			model.addAttribute("itemCount", itemService.count());
			model.addAttribute("userCount", userService.count());
			model.addAttribute("blogCount", blogService.count());
			model.addAttribute("lastIndexedDateFinish", blogService.getLastIndexedDateFinish());
			showAll = true;
		}
		model.addAttribute("items", itemService.getDtoItems(0, showAll));
		model.addAttribute("nextPage", 1);
		return "index";
	}

	@RequestMapping(value = "/index", params = "page")
	public String index(Model model, @RequestParam int page, HttpServletRequest request) {
		boolean showAll = false;
		if (request.isUserInRole("ROLE_ADMIN")) {
			showAll = true;
		}
		model.addAttribute("items", itemService.getDtoItems(page, showAll));
		model.addAttribute("nextPage", page + 1);
		return "index";
	}

	@ResponseBody
	@RequestMapping("/page/{page}")
	public List<ItemDto> getPage(@PathVariable int page, HttpServletRequest request) {
		boolean showAll = false;
		if (request.isUserInRole("ROLE_ADMIN")) {
			showAll = true;
		}
		return itemService.getDtoItems(page, showAll);
	}
}
