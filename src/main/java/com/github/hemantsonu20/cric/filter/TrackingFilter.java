package com.github.hemantsonu20.cric.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackingFilter extends AbstractFilter {

	private static final Logger LOG = LoggerFactory.getLogger(TrackingFilter.class);
	private static final String TRACKING_ID = "trackingId";

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException,
			ServletException {

		String trackingId = req.getHeader(TRACKING_ID);
		if(StringUtils.isBlank(trackingId)) {
			trackingId = UUID.randomUUID().toString();
		}
		MDC.put(TRACKING_ID, trackingId);
		
		LOG.info("{} {}", req.getMethod(), req.getRequestURI());
		try {
			res.setHeader(TRACKING_ID, trackingId);
			chain.doFilter(req, res);
		} finally {
			LOG.info("Response status {}", res.getStatus());
			MDC.clear();
		}
	}
}
