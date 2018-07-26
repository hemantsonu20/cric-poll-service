package com.github.hemantsonu20.cric.exception;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

	private static final String UNEXPECTED_ERROR = "Exception.unexpected";
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

	private final MessageSource messageSource;

	@Autowired
	public ExceptionControllerAdvice(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> errorMessages =
				ex
						.getBindingResult()
						.getAllErrors()
						.stream()
						.map(objectError -> messageSource.getMessage(objectError, LocaleContextHolder.getLocale()))
						.collect(Collectors.toList());

		ErrorDetails errorDetails =
				new ErrorDetails(status.value(), "Validation Failed", errorMessages);
		return new ResponseEntity<>(errorDetails, headers, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
		}
		ErrorDetails error = new ErrorDetails(status.value(), ex.getMessage(), request.getDescription(false));
		LOG.error(ex.getMessage(), ex);
		return new ResponseEntity<>(error, headers, status);
	}
	

	@ExceptionHandler(PollException.class)
	public ResponseEntity<ErrorDetails> handlePollException(PollException ex, WebRequest request) {
		ErrorDetails errorDetails =
				new ErrorDetails(ex.getHttpStatus().value(), ex.getMessage(), request.getDescription(false));
		LOG.error(ex.getMessage(), ex);
		return new ResponseEntity<>(errorDetails, ex.getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleExceptions(Exception ex, Locale locale) {
		String errorMessage = messageSource.getMessage(UNEXPECTED_ERROR, null, locale);
		LOG.error(errorMessage, ex);
		return new ResponseEntity<>(
				new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), errorMessage),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
