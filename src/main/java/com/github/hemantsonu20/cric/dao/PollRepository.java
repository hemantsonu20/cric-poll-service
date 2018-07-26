package com.github.hemantsonu20.cric.dao;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hemantsonu20.cric.exception.PollException;
import com.github.hemantsonu20.cric.model.Auction;
import com.github.hemantsonu20.cric.model.Poll;
import com.github.hemantsonu20.cric.model.UserTag;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

@Component
public class PollRepository {
	
	@Autowired
	private ObjectMapper mapper;

	private final CollectionReference dao = FirestoreClient.getFirestore().collection("polls");

	public Poll createPoll(Poll poll) {

		try {
			ApiFuture<DocumentReference> future = dao.add(poll);
			poll.setId(future.get().getId());
			return poll;
		} catch (Exception e) {
			throw new PollException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create poll", e);
		}
	}

	public Poll getPoll(String id) {

		try {
			ApiFuture<DocumentSnapshot> future = dao.document(id).get();
			DocumentSnapshot doc = future.get();
			if (doc.exists()) {
				Poll poll = doc.toObject(Poll.class);
				poll.setId(doc.getId());
				return poll;
			} else {
				throw new PollException(HttpStatus.NOT_FOUND, "Poll doesn't exist");
			}
		} catch (Exception e) {
			throw new PollException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get poll", e);
		}
	}

	public void deletePoll(String id) {
		try {
			dao.document(id).delete().get();
		} catch (Exception e) {
			throw new PollException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete poll", e);
		}
	}

	public List<Poll> getPolls(String email) {

		ApiFuture<QuerySnapshot> future = dao.whereEqualTo("ownerEmail", email).get();
		try {
			QuerySnapshot docs = future.get();
			return docs.getDocuments().stream().map(q -> {
				Poll poll = q.toObject(Poll.class);
				poll.setId(q.getId());
				return poll;
			}).sorted(Comparator.comparingLong(Poll::getTimestamp).reversed()).collect(Collectors.toList());

		} catch (Exception e) {
			throw new PollException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get polls", e);
		}
	}

	public Poll addUserTag(String id, UserTag userTag) {
		try {
			dao
					.document(id)
					.update(
							FieldPath.of("userTags", userTag.getName(), "name"),
							userTag.getName(),
							FieldPath.of("userTags", userTag.getName(), "vote"),
							userTag.getVote(),
							FieldPath.of("userTags", userTag.getName(), "team"),
							userTag.getTeam())
					.get();
		} catch (Exception e) {
			throw new PollException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add vote in poll", e);
		}
		return getPoll(id);
	}

	public Poll auctionNomination(String currentUserEmail, String id, Auction auction) {
		Poll poll = getPoll(id);
		if (!poll.getOwnerEmail().equalsIgnoreCase(currentUserEmail)) {
			throw new PollException(HttpStatus.FORBIDDEN, "You are not authorized to perform this operation");
		}
		try {
			dao.document(id).update("auction", getPojoAsMap(auction)).get();
		} catch (Exception e) {
			throw new PollException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add auction in poll", e);
		}
		return getPoll(id);
	}

	public Poll auctionSelection(String currentUserEmail, String id, UserTag userTag) {
		Poll poll = getPoll(id);
		if (!poll.getAuction().getNext().equalsIgnoreCase(currentUserEmail)) {
			throw new PollException(HttpStatus.FORBIDDEN, "You are not authorized to perform this operation");
		}

		DocumentReference documentRef = dao.document(id);
		ApiFuture<Poll> future = FirestoreClient.getFirestore().runTransaction(transaction -> {

			transaction.update(
					documentRef,
					FieldPath.of("userTags", userTag.getName(), "name"),
					userTag.getName(),
					FieldPath.of("userTags", userTag.getName(), "vote"),
					userTag.getVote(),
					FieldPath.of("userTags", userTag.getName(), "team"),
					userTag.getTeam());
			transaction
					.update(documentRef, "auction.next", getNextAuctionSelector(poll.getAuction(), currentUserEmail));
			return transaction.get(documentRef).get().toObject(Poll.class);
		});
		try {
			return future.get();
		} catch (Exception e) {
			throw new PollException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to select player in auction", e);
		}
	}

	private String getNextAuctionSelector(Auction auction, String currentUserEmail) {
		if (auction.getFirstTeamOwner().equals(currentUserEmail)) {
			return auction.getSecondTeamOwner();
		}
		return auction.getFirstTeamOwner();
	}
	
	Map<String, Object> getPojoAsMap(Object obj) {
		
		try {
			String json = mapper.writeValueAsString(obj);
			return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});
		}			
		catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
