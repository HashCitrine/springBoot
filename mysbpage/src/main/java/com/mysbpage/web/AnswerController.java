package com.mysbpage.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysbpage.domain.Answer;
import com.mysbpage.domain.AnswerRepository;
import com.mysbpage.domain.Question;
import com.mysbpage.domain.QuestionRepository;

@Controller
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {

	@Autowired
	AnswerRepository answerRepository;
	
	@Autowired
	QuestionRepository questionRepository;
	
	@PostMapping("")
	public String answer(@PathVariable Long questionId, String contents, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session) ) {
			return "/users/login";
		}
		Question question = questionRepository.getOne(questionId);
		answerRepository.save(new Answer(HttpSessionUtils.getUserFromSession(session), contents, question));
		return String.format("redirect:/questions/%d", questionId);
	}
	
}
