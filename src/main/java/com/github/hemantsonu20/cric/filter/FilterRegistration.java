package com.github.hemantsonu20.cric.filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterRegistration {

	
	@ConditionalOnBean(AuthFilter.class)
	@Bean
	public FilterRegistrationBean<AuthFilter> authFilterRegBean(AuthFilter authFilter) {

		FilterRegistrationBean<AuthFilter> reg = new FilterRegistrationBean<>();
		reg.setFilter(authFilter);
		reg.setOrder(Ordered.HIGHEST_PRECEDENCE + 200);
		return reg;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {

		FilterRegistrationBean<CorsFilter> reg = new FilterRegistrationBean<>();
		reg.setFilter(new CorsFilter());
		reg.setOrder(Ordered.HIGHEST_PRECEDENCE + 100);
		return reg;
	}
	
	@Bean
	public FilterRegistrationBean<TrackingFilter> loggingFilter() {

		FilterRegistrationBean<TrackingFilter> reg = new FilterRegistrationBean<>();
		reg.setFilter(new TrackingFilter());
		reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return reg;
	}
}
