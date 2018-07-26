package com.github.hemantsonu20.cric.model;

import javax.validation.constraints.NotBlank;

public class Auction extends BaseBean {

	@NotBlank
	private String firstTeam;
	@NotBlank
	private String firstTeamOwner;
	@NotBlank
	private String secondTeam;
	@NotBlank
	private String secondTeamOwner;
	@NotBlank
	private String next;

	public String getFirstTeam() {
		return firstTeam;
	}

	public void setFirstTeam(String firstTeam) {
		this.firstTeam = firstTeam;
	}

	public String getFirstTeamOwner() {
		return firstTeamOwner;
	}

	public void setFirstTeamOwner(String firstTeamOwner) {
		this.firstTeamOwner = firstTeamOwner;
	}

	public String getSecondTeam() {
		return secondTeam;
	}

	public void setSecondTeam(String secondTeam) {
		this.secondTeam = secondTeam;
	}

	public String getSecondTeamOwner() {
		return secondTeamOwner;
	}

	public void setSecondTeamOwner(String secondTeamOwner) {
		this.secondTeamOwner = secondTeamOwner;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}
}
