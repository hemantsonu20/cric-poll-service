package com.github.hemantsonu20.cric.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

public class CorsFilter extends AbstractFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException,
			ServletException {

		res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, PUT, POST, DELETE");
		
		// wild card is not yet supported on some browser
		if(req.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS) != null) {
			res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Accept, Accept-Language, Content-Language, Content-Type, X_AUTH_TOKEN");
		}
		else {
			res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
		}
		if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
			res.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}
		chain.doFilter(req, res);
	}
}
