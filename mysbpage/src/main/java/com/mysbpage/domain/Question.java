package com.mysbpage.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Question {
	@Id
	@GeneratedValue
	private Long id;
	
//	private String writer;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;		// 객체지향적인 관계를 형성하기 위해 -> 작성된 게시글의 작성자 정보 전체(User형 데이터)를 저장
	
	private String title;
	private String contents;
	private LocalDateTime createDate;
	
	public Question() {}
	public Question(User writer, String title, String contents) {
		super();
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.createDate = LocalDateTime.now();
	}
	
	public String getFormattedCreateDate() {
		if(createDate == null) {
			return "";
		}
		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.DD HH:mm:ss"));
	}
	public void update(String updateTitle, String updateContents) {
		this.title = updateTitle;
		this.contents = updateContents;
	}
}
