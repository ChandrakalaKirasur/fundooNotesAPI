package com.bridgelabz.fundoonotes.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bridgelabz.fundoonotes.response.RequestInterceptors;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
	
	@Autowired
	private RequestInterceptors interceptors;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptors).addPathPatterns("/notes");
	}
}
