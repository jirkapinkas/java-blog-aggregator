package cz.jiripinkas.jba.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
		if (request.isUserInRole("ROLE_ADMIN")) {
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

	private MaxType resolveMaxType(String max) {
		MaxType maxType = MaxType.UNDEFINED;
		if ("month".equals(max)) {
			maxType = MaxType.MONTH;
		} else if ("week".equals(max)) {
			maxType = MaxType.WEEK;
		}
		return maxType;
	}

	private String resolveTitle(MaxType maxType) {
		String finalTitle = configurationService.find().getTopHeading();
		switch (maxType) {
		case MONTH:
			finalTitle += " this month: ";
			break;
		case WEEK:
			finalTitle += " this week: ";
			break;
		default:
			finalTitle += ": ";
			break;
		}
		return finalTitle;
	}

	private MaxType populateModelWithMax(Model model, String max) {
		model.addAttribute("topViews", true);
		MaxType maxType = resolveMaxType(max);
		if (maxType != MaxType.UNDEFINED) {
			model.addAttribute("max", true);
			model.addAttribute("maxValue", max);
		}
		model.addAttribute("title", resolveTitle(maxType));
		return maxType;
	}

	@RequestMapping(value = "/index", params = "top-views")
	public String topViews(Model model, HttpServletRequest request, @RequestParam(required = false) String max) {
		MaxType maxType = populateModelWithMax(model, max);
		return showFirstPage(model, request, "top-views", OrderType.MOST_VIEWED, maxType);
	}

	@RequestMapping(value = "/index", params = { "page", "top-views" })
	public String topViews(Model model, @RequestParam int page, HttpServletRequest request, @RequestParam(required = false) String max) {
		MaxType maxType = populateModelWithMax(model, max);
		return showPage(model, request, page, "top-views", OrderType.MOST_VIEWED, maxType);
	}

	@ResponseBody
	@RequestMapping("/page/{page}")
	public List<ItemDto> getPageLatest(@PathVariable int page, HttpServletRequest request, @RequestParam Integer[] selectedCategories) {
		boolean showAll = false;
		if (request.isUserInRole("ROLE_ADMIN")) {
			showAll = true;
		}
		return itemService.getDtoItems(page, showAll, OrderType.LATEST, MaxType.UNDEFINED, selectedCategories);
	}

	@ResponseBody
	@RequestMapping(value = "/page/{page}", params = "topviews")
	public List<ItemDto> getPageMostViewed(@PathVariable int page, HttpServletRequest request, @RequestParam(required = false) String max, @RequestParam Integer[] selectedCategories) {
		boolean showAll = false;
		if (request.isUserInRole("ROLE_ADMIN")) {
			showAll = true;
		}
		return itemService.getDtoItems(page, showAll, OrderType.MOST_VIEWED, resolveMaxType(max), selectedCategories);
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

}
