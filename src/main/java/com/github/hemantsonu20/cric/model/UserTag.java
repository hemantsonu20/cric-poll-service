package com.github.hemantsonu20.cric.model;

import javax.validation.constraints.NotBlank;

public class UserTag {

	@NotBlank
	private String name;
	
	@NotBlank
	private String vote;
	
	@NotBlank
	private String team;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVote() {
		return vote;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}
}
