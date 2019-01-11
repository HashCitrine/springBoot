package com.mysbpage.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoy extends JpaRepository<User, Long>{
	User findByUserId(String userId);
}
