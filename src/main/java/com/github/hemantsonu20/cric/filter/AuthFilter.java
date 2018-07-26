package com.github.hemantsonu20.cric.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hemantsonu20.cric.exception.ErrorDetails;
import com.github.hemantsonu20.cric.model.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@Component
public class AuthFilter extends AbstractFilter {

	@Autowired
	private ObjectMapper mapper;

	private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

	public static final String TOKEN_HEADER = "X_AUTH_TOKEN";
	public static final String CURRENT_FIREBASE_USER = "CURRENT_FIREBASE_USER";
	private static final String AUTH_ERROR_MSG = "X_AUTH_TOKEN header is invalid / not present in request";

	private static final Map<String, List<String>> IGNORED_MAPPINGS = new HashMap<>();

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException,
			ServletException {

		if (!ignored(req)) {

			String authHeader = req.getHeader(TOKEN_HEADER);
			if (StringUtils.isBlank(authHeader) || isInvalidAuth(req, authHeader)) {
				LOG.error(AUTH_ERROR_MSG);
				res.getWriter().write(
						mapper.writeValueAsString(new ErrorDetails(401, "Unauthorized Request", AUTH_ERROR_MSG)));
				res.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
				res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}
		chain.doFilter(req, res);
	}

	protected boolean ignored(HttpServletRequest req) {

		String uri = req.getRequestURI();
		if (!uri.startsWith("/api")) {
			return true;
		}

		AntPathMatcher matcher = new AntPathMatcher();
		List<String> mappings = IGNORED_MAPPINGS.getOrDefault(req.getMethod().toUpperCase(), Collections.emptyList());
		for (String m : mappings) {
			if (matcher.match(m, uri)) {
				return true;
			}
		}
		return false;
	}

	private boolean isInvalidAuth(HttpServletRequest req, String authHeader) {

		try {
			FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(authHeader);
			FirebaseUser user = new FirebaseUser(token.getUid(), token.getEmail(), token.getName());
			req.setAttribute(CURRENT_FIREBASE_USER, user);
			return false;
		} catch (FirebaseAuthException | IllegalArgumentException e) {
			LOG.error(AUTH_ERROR_MSG, e);
			return true;
		}
	}

	static {
		IGNORED_MAPPINGS.put("GET", Arrays.asList("/api/v*/polls/*"));
		IGNORED_MAPPINGS.put("POST", Arrays.asList("/api/v*/polls/*/vote"));
	}
}
