package com.github.hemantsonu20.cric.exception;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * A controller which adds additional details in case of some error occurred in
 * the service
 * 
 * @author pratapihemant.patel
 *
 */
@Controller
public class ErrorController extends BasicErrorController {

    private final ErrorAttributes errorAttributes;

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes, new ErrorProperties());
        this.errorAttributes = errorAttributes;
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {

        Map<String, Object> details = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        Throwable throwable = errorAttributes.getError(new ServletWebRequest(request));

        Map<String, Object> body = new HashMap<>();
        HttpStatus status;
        if (throwable instanceof WebServiceException) {
            status = ((WebServiceException) throwable).getHttpStatus();
        }
        else {
            status = getStatus(request);
        }
        body.put("code", status.value());
        body.put("message", "Unexpected Error Occurred");
        body.put("details", details);
        return new ResponseEntity<>(body, status);
    }
    
    @Override
    @RequestMapping("/asfasfadsdafasfasfasfasf")
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
    	return super.errorHtml(request, response);
    }
}
