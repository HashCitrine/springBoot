package com.mysbpage.web;

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
//
//@Controller
//@RequestMapping("/users")
//public class UserController {
//	
//	//	userDAO와 동일한 역할
//	//	DB에 접속하고 자동으로 커넥션풀을 생성 및 종료
//	@Autowired
//	private UserRepositoy userRepository;
//	
//	@PostMapping("")
//	public String create(User user) {
//		System.out.println("user : " + user);
//		userRepository.save(user);	//DB에 저장
//		return "redirect:/users";
//	}
//	@PostMapping("loginaction")
//	public String loginAction(String userId, String password, HttpSession session) {
//		User user = userRepository.findByUserId(userId);
//		if(user == null) {
//			return "redirect:/users/login";
//		}
//		
//		if(!password.equals(user.getPassword())) {
//			return "redirect:/users/login";
//		}
//		
//		session.setAttribute("sessionedUser", user); 	// session에 user라는 이름으로 user값() 저장
//		System.out.println("Login Success");
//		return "redirect:/index";
//	}
//	@GetMapping("")
//	public String list(Model model) {
//		model.addAttribute("users", userRepository.findAll());
//		return "/user/list";
//	}
//	@GetMapping("/form")
//	public String form() {
//		return "/user/form";
//	}
//	@GetMapping("/login")
//	public String loginform() {
//		return "/user/login";
//	}
//	@GetMapping("/logout")
//	public String logout(HttpSession session) {
//		session.removeAttribute("sessionedUser");
//		return "redirect:/";
//	}
//	@GetMapping("/{id}/update")
//	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
//		Object tempUser = session.getAttribute("sessionedUser");
//		if(tempUser == null) {
//			return "redirect:/users/login";
//		}
//		
//		User sessionedUser = (User)tempUser;
//		// 다른 사용자의 정보 수정을 막는 방법
//		// 방법 1 : session에 저장된 user데이터의 id값을 이용해 비교
//		if(!id.equals(sessionedUser.getId())) {
//			throw new IllegalStateException("You can't access this page");
//		}
//		// 방법 2 : findById(id).get -> findById(sessionedUser.getId())
//		model.addAttribute("user", userRepository.findById(id).get());  
//		return "/user/updateForm";
//	}
//	@PutMapping("{id}")
//	public String update(@PathVariable Long id, User updateUser, HttpSession session) {
//		Object tempUser = session.getAttribute("sessionedUser");
//		if(tempUser == null) {
//			return "redirect:/users/login";
//		}
//		
//		User sessionedUser = (User)tempUser;
//		if(!id.equals(sessionedUser.getId())) {
//			throw new IllegalStateException("You can't access this page");
//		}
//		
//		User user = userRepository.findById(id).get();
//		user.update(updateUser);
//		userRepository.save(user);
//		return "redirect:/users";
//	}
//}


// HttpSessionUtils.java 작성 후 : 리팩토링(refactoring)
// 1. sessionedUser => HttpSessionUtils.USER_SESSION_KEY
// 2. 
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
// 2. password.equals(user.getPassword()) => user.matchpassword(password)
		if(!user.matchpassword(password)) {
			return "redirect:/users/login";
		}
		
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user); 	// session에 user라는 이름으로 user값() 저장
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
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		return "redirect:/";
	}
	
// 3. tempuser => HttpSessionUtils.isLoginUser(session)
	@GetMapping("/{id}/update")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		//
		if(HttpSessionUtils.isLoginUser(session)) {
			return "redirect:/users/login";
		}
							//
		User sessionedUser = HttpSessionUtils.getUserFromSession(session);
		// 다른 사용자의 정보 수정을 막는 방법
		// 방법 1 : session에 저장된 user데이터의 id값을 이용해 비교

// 4. id.equals(sessionedUser.getId()) => sessionedUser.matchId(id)
		if(!sessionedUser.matchId(id)) {
			throw new IllegalStateException("You can't access this page");
		}
		// 방법 2 : findById(id).get -> findById(sessionedUser.getId())
		model.addAttribute("user", userRepository.findById(id).get());  
		return "/user/updateForm";
	}
	@PutMapping("{id}")
	public String update(@PathVariable Long id, User updateUser, HttpSession session) {
		Object tempUser = session.getAttribute(HttpSessionUtils.USER_SESSION_KEY);
		if(tempUser == null) {
			return "redirect:/users/login";
		}
		
		User sessionedUser = (User)tempUser;
		if(!sessionedUser.matchId(id)) {
			throw new IllegalStateException("You can't access this page");
		}
		
		User user = userRepository.findById(id).get();
		user.update(updateUser);
		userRepository.save(user);
		return "redirect:/users";
	}
}
