package cz.jiripinkas.jba.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RoadmapController {
	
	@RequestMapping("/roadmap")
	public String roadmap() {
		return "roadmap";
	}
	
}
