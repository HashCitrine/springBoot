package com.mysbpage.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysbpage.domain.User;
import com.mysbpage.domain.UserRepositoy;



/* DB 적용 전 : List<User>형 데이터 이용
@Controller
public class UserController {
	private List<User> users = new ArrayList<User>();
	
	@PostMapping("/create")
	public String create(User user) {
		System.out.println("user : " + user);
		users.add(user);
		return "redirect:/list";
	}
	
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("users", users);
		return "list";
	}
}
*/

// DB적용 후 : 인터페이스(UserRepository) 이용
@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserRepositoy userRepository;
	
	@PostMapping("")
	public String create(User user) {
		System.out.println("user : " + user);
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "/user/list";
	}
	@GetMapping("/form")
	public String form() {
		return "/user/form";
	}
	@GetMapping("/login")
	public String login() {
		return "/user/login";
	}
	
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model) {
		model.addAttribute("user", userRepository.findById(id).get());
		return "/user/updateForm";
	}
	@PutMapping("{id}")
	public String update(@PathVariable Long id, User updateUser) {
		User user = userRepository.findById(id).get();
		user.update(updateUser);
		userRepository.save(user);
		return "redirect:/users";
	}
}
