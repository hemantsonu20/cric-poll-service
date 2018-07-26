package com.github.hemantsonu20.cric.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.hemantsonu20.cric.aspect.Loggable;
import com.github.hemantsonu20.cric.dao.PollRepository;
import com.github.hemantsonu20.cric.filter.AuthFilter;
import com.github.hemantsonu20.cric.model.FirebaseUser;
import com.github.hemantsonu20.cric.model.Poll;
import com.github.hemantsonu20.cric.model.UserTag;

@RestController
public class PollController {

	private final PollRepository repository;

	public PollController(PollRepository repository) {
		this.repository = repository;
	}

	@Loggable
	@RequestMapping(value = "/api/v1/polls", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Poll createPoll(@Valid @RequestBody Poll poll, HttpServletRequest req) {

		FirebaseUser user = (FirebaseUser) req.getAttribute(AuthFilter.CURRENT_FIREBASE_USER);
		poll.setOwnerEmail(user.getEmail());
		poll.setOwnerName(user.getName());
		return repository.createPoll(poll);
	}
	
	@Loggable
	@RequestMapping(value = "/api/v1/polls/{id}", method = RequestMethod.GET)
	public Poll getPoll(@PathVariable String id) {

		return repository.getPoll(id);
	}
	
	@Loggable
	@RequestMapping(value = "/api/v1/polls/{id}", method = RequestMethod.DELETE)
	public void deletePoll(@PathVariable String id) {
		repository.deletePoll(id);
	}
	
	@Loggable
	@RequestMapping(value = "/api/v1/polls", method = RequestMethod.GET)
	public List<Poll> getPolls(HttpServletRequest req) {

		FirebaseUser user = (FirebaseUser) req.getAttribute(AuthFilter.CURRENT_FIREBASE_USER);
		return repository.getPolls(user.getEmail());
	}
	
	@Loggable
	@RequestMapping(value = "/api/v1/polls/{id}/vote", method = RequestMethod.POST)
	public Poll addUserTag(@PathVariable String id, @Valid @RequestBody UserTag userTag) {

		return repository.addUserTag(id, userTag);
	}
}
