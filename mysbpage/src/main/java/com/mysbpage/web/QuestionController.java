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
import com.mysbpage.domain.Result;
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
	
	@PutMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		Question question = questionRepository.findById(id).get();
		Result result = valid(session,question);
		if(!result.isValid()) {
			model.addAttribute("errorMessage", result.getErrorMessage());
			return "/user/login";
		}
			model.addAttribute("show", question);
			return "/qna/update";
	}
	
	private Result valid(HttpSession session, Question question) {		// result형의 valid 메소드 생성
		if(!HttpSessionUtils.isLoginUser(session) ) {
			return Result.fail("로그인이 필요합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if(!question.isSameWirter(loginUser)) {
			return Result.fail("자신이 작성한 글만 수정, 삭제할 수 있습니다.");
		}
		return Result.ok();
	}
	
//	private boolean hasPermission(HttpSession session, Question question) {
//		if(!HttpSessionUtils.isLoginUser(session) ) {
//			System.out.println("Must Login");
//			throw new IllegalStateException("로그인이 필요합니다.");
//		}
//		User loginUser = HttpSessionUtils.getUserFromSession(session);
//		if(!question.isSameWirter(loginUser)) {
//			System.out.println("Only Writer");
//			throw new IllegalStateException("자신이 작성한 글만 수정, 삭제할 수 있습니다.");
//		}
//		return true;
//	}
	
	@PutMapping("{id}")
	public String update(@PathVariable Long id, String title, String contents, HttpSession session, Model model) {
		Question question = questionRepository.findById(id).get();
		Result result = valid(session,question);
		if(!result.isValid()) {
			model.addAttribute("errorMessage", result.getErrorMessage());
			return "/user/login";
		}
			question.update(title,contents);
			questionRepository.save(question);
			return String.format("redirect:/questions/%d", id);
	}
			
	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id, Model model, HttpSession session) {
		Question question = questionRepository.findById(id).get();
		Result result = valid(session,question);
		if(!result.isValid()) {
			model.addAttribute("errorMessage", result.getErrorMessage());
			return "/user/login";
		}
			questionRepository.deleteById(id);			
			return "redirect:/";
	}
}
