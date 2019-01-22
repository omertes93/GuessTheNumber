package com.omer.guessthenumber;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;

@Entity
@Table(name = "games")
public class TopScoresEntity {

	@Id
	@GeneratedValue
	@Column(name = "idgame")
	private Long id;
	@Column
	private String fullname;

	@Column
	private Integer attemptscount;


	public TopScoresEntity(Long id, String fullname, Integer attemptscount) {
		this.id = id;
		this.fullname = fullname;
		this.attemptscount = attemptscount;
	}

	public TopScoresEntity() {

	}

	public Integer getAttemptscount() {
		return attemptscount;
	}

	public void setAttemptscount(Integer attemptscount) {
		this.attemptscount = attemptscount;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getCounter() {
		return attemptscount;
	}

	public void setCounter(Integer counter) {
		this.attemptscount = counter;
	}

}
