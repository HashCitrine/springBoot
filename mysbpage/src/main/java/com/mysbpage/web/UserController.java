package com.mysbpage.web;

import javax.persistence.metamodel.SetAttribute;
import javax.servlet.http.HttpSession;

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
	
	//	userDAO와 동일한 역할
	//	DB에 접속하고 자동으로 커넥션풀을 생성 및 종료
	@Autowired
	private UserRepositoy userRepository;
	
	@PostMapping("")
	public String create(User user) {
		System.out.println("user : " + user);
		userRepository.save(user);	//DB에 저장
		return "redirect:/users";
	}
	@PostMapping("loginaction")
	public String loginAction(String userId, String password, HttpSession session) {
		User user = userRepository.findByUserId(userId);
		if(user == null) {
			return "redirect:/users/login";
		}
		
		if(!password.equals(user.getPassword())) {
			return "redirect:/users/login";
		}
		
		session.setAttribute("user", user); 	// session에 user라는 이름으로 user값() 저장
		System.out.println("Login Success");
		return "redirect:/index";
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
	public String loginform() {
		return "/user/login";
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:/";
	}
	@GetMapping("/update/{id}")
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
