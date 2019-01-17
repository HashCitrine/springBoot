package com.mysbpage.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysbpage.domain.Question;
import com.mysbpage.domain.QuestionRepository;
import com.mysbpage.domain.User;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@GetMapping("/form")
	public String form(HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/users/login";
		}
		return "/qna/form";
	}
	
	@PostMapping("/write")
	public String write(HttpSession session, String title, String contents) {
		if(!HttpSessionUtils.isLoginUser(session) ) {
			return "/users/login";
		}
		
		User sessionUser = HttpSessionUtils.getUserFromSession(session);
		Question newQuestion = new Question(sessionUser, title, contents);
		questionRepository.save(newQuestion);
		return "redirect:/index";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable Long id, Model model) {
		model.addAttribute("question", questionRepository.findById(id).get());
		return "/qna/show";
	}
	
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {

		model.addAttribute("show", questionRepository.findById(id).get());
		return "/qna/update";
	}
	
	@PutMapping("{id}")
	public String update(@PathVariable Long id, String title, String contents, HttpSession session) {
		Object tempUser = session.getAttribute(HttpSessionUtils.USER_SESSION_KEY);
		if(tempUser == null) {
			return "redirect:/users/login";
		}
		
		User sessionedUser = (User)tempUser;
		if(!sessionedUser.matchId(id)) {
			throw new IllegalStateException("You can't access this page");
		}
		
		Question updatedQuestion = questionRepository.findById(id).get();
		updatedQuestion.update(title,contents);
		questionRepository.save(updatedQuestion);
		return String.format("redirect:/questions/%d", id);
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id) {
		questionRepository.deleteById(id);
		return "redirect:/";
	}
}
