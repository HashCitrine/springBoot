package com.mysbpage.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mysbpage.domain.QuestionRepository;

@Controller
public class WebController {
	 
	@Autowired
	private QuestionRepository questionRepository;
	
	@GetMapping("/index")
	public String index(Model model) {
		model.addAttribute("questions", questionRepository.findAll());
		return "/index";
	}
}
