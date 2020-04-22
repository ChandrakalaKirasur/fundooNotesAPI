package com.bridgelabz.fundoonotes.response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bridgelabz.fundoonotes.exception.InvalidHeaderFieldException;

import io.swagger.annotations.Api;

@Component
@Api("/notes")
public class RequestInterceptors extends HandlerInterceptorAdapter{
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception {
		if(StringUtils.isBlank(request.getHeader("token"))) {
			throw new InvalidHeaderFieldException("Invalid request",406);
		}
		return super.preHandle(request, response, handler);
	}
}
