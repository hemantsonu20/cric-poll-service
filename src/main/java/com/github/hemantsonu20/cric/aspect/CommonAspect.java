package com.github.hemantsonu20.cric.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CommonAspect {

	private static final Logger LOG = LoggerFactory.getLogger(CommonAspect.class);
	
	@Around("execution(* *(..)) && @annotation(com.github.hemantsonu20.cric.aspect.Loggable)")
	public Object logEntryExit(ProceedingJoinPoint pjp) throws Throwable {

		Loggable ann = getAnnotation(pjp, Loggable.class);
		if (ann.enable()) {
			return log(pjp);
		}
		else {
			return pjp.proceed();
		}
	}

	private Object log(ProceedingJoinPoint pjp) throws Throwable {
		
		String methodName = pjp.getSignature().getName();
		String className = pjp.getTarget().getClass().getSimpleName();
		
		try {
			LOG.info("Entering method {}#{} with args {}", className, methodName, getArgs(pjp));
			Object toBeReturned =  pjp.proceed();
			LOG.info("Exiting method {}#{} returning {}", className, methodName, toBeReturned);
			return toBeReturned;
		}
		catch(Throwable th) {
			LOG.info("Exiting method {}#{} with exception", className, methodName, th);
			throw th;
		}
	}

	private String getArgs(JoinPoint joinPoint) {
		
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		String[] parameterNames = signature.getParameterNames();
		Object[] parameterValues = joinPoint.getArgs();
		StringBuilder args = new StringBuilder();
		for(int i = 0; i < parameterNames.length; i++) {
			args.append(parameterNames[i]).append("=").append(parameterValues[i]).append(", ");
		}
		return args.toString();
	}

	private <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotationClass) {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		return method.getAnnotation(annotationClass);
	}
}
