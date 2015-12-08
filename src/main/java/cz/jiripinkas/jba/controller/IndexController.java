package cz.jiripinkas.jba.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.jiripinkas.jba.dto.ItemDto;
import cz.jiripinkas.jba.service.AllCategoriesService;
import cz.jiripinkas.jba.service.ConfigurationService;
import cz.jiripinkas.jba.service.ItemService;
import cz.jiripinkas.jba.service.ItemService.MaxType;
import cz.jiripinkas.jba.service.ItemService.OrderType;

@Controller
public class IndexController {

	private static final Logger log = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private ItemService itemService;

	@Autowired
	private AllCategoriesService allCategoriesService;

	@Autowired
	private ConfigurationService configurationService;

	private String showFirstPage(Model model, HttpServletRequest request, String tilesPage, OrderType orderType, MaxType maxType) {
		return showPage(model, request, 0, tilesPage, orderType, maxType);
	}

	private String showPage(Model model, HttpServletRequest request, int page, String tilesPage, OrderType orderType, MaxType maxType) {
		boolean showAll = false;
		if (request.isUserInRole("ADMIN")) {
			showAll = true;
		}
		model.addAttribute("items", itemService.getDtoItems(page, showAll, orderType, maxType, allCategoriesService.getAllCategoryIds()));
		model.addAttribute("nextPage", page + 1);
		return tilesPage;
	}

	@RequestMapping("/index")
	public String index(Model model, HttpServletRequest request) {
		model.addAttribute("title", configurationService.find().getHomepageHeading());
		return showFirstPage(model, request, "index", OrderType.LATEST, MaxType.UNDEFINED);
	}

	@RequestMapping(value = "/index", params = "page")
	public String index(Model model, @RequestParam int page, HttpServletRequest request) {
		model.addAttribute("title", configurationService.find().getHomepageHeading());
		return showPage(model, request, page, "index", OrderType.LATEST, MaxType.UNDEFINED);
	}

	@ResponseBody
	@RequestMapping("/page/{page}")
	public List<ItemDto> getPageLatest(@PathVariable int page, HttpServletRequest request, @RequestParam Integer[] selectedCategories, @RequestParam(required = false) String search,
			@RequestParam(required = false) String orderBy) {
		if (search != null && !search.trim().isEmpty()) {
			log.info("search for: " + search);
		}
		boolean showAll = false;
		if (request.isUserInRole("ADMIN")) {
			showAll = true;
		}
		if ("topWeek".equals(orderBy)) {
			return itemService.getDtoItems(page, showAll, OrderType.MOST_VIEWED, MaxType.WEEK, selectedCategories, search);
		} else if ("topMonth".equals(orderBy)) {
			return itemService.getDtoItems(page, showAll, OrderType.MOST_VIEWED, MaxType.MONTH, selectedCategories, search);
		} else {
			return itemService.getDtoItems(page, showAll, OrderType.LATEST, MaxType.UNDEFINED, selectedCategories, search);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/inc-count", method = RequestMethod.POST)
	public String incItemCount(@RequestParam int itemId) {
		return Integer.toString(itemService.incCount(itemId));
	}

	@RequestMapping(value = "/icon", produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] getIcon() throws IOException {
		return configurationService.find().getIcon();
	}

	@RequestMapping(value = "/favicon", produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] getFavicon() throws IOException {
		return configurationService.find().getFavicon();
	}

	@RequestMapping(value = "/appleTouchIcon", produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] getAppleTouchIcon() throws IOException {
		return configurationService.find().getAppleTouchIcon();
	}

}
