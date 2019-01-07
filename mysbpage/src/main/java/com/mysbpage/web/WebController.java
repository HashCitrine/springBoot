package com.mysbpage.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
	@GetMapping("index")
	public String index() {
		return "index";
	}
}
