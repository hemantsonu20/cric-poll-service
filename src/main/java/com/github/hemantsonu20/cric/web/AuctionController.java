package com.github.hemantsonu20.cric.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.hemantsonu20.cric.dao.PollRepository;
import com.github.hemantsonu20.cric.filter.AuthFilter;
import com.github.hemantsonu20.cric.model.Auction;
import com.github.hemantsonu20.cric.model.FirebaseUser;
import com.github.hemantsonu20.cric.model.Poll;
import com.github.hemantsonu20.cric.model.UserTag;

@RestController
public class AuctionController {
	private final PollRepository repository;

	public AuctionController(PollRepository repository) {
		this.repository = repository;
	}

	@RequestMapping(value = "/api/v1/polls/{id}/auction/nomination", method = RequestMethod.POST)
	public
			Poll
			auctionNomination(@PathVariable String id, @Valid @RequestBody Auction auction, HttpServletRequest req) {

		FirebaseUser user = (FirebaseUser) req.getAttribute(AuthFilter.CURRENT_FIREBASE_USER);
		return repository.auctionNomination(user.getEmail(), id, auction);
	}
	
	@RequestMapping(value = "/api/v1/polls/{id}/auction/selection", method = RequestMethod.POST)
	public
			Poll
			auctionSelection(@PathVariable String id, @Valid @RequestBody UserTag userTag, HttpServletRequest req) {

		FirebaseUser user = (FirebaseUser) req.getAttribute(AuthFilter.CURRENT_FIREBASE_USER);
		return repository.auctionSelection(user.getEmail(), id, userTag);
	}
}
