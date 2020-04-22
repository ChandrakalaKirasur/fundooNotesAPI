package com.bridgelabz.fundoonotes.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
//@Component
public class MyFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	/*
	 * @Autowired private UserServiceImpl userService;
	 * 
	 * @Autowired private JWTUtil util;
	 * 
	 * @Override protected void doFilterInternal(HttpServletRequest request,
	 * HttpServletResponse response, FilterChain filterChain) throws
	 * ServletException, IOException { final String
	 * authorizationHeader=request.getHeader("Authorization"); String userName=null;
	 * String jwt=null; if(authorizationHeader != null &&
	 * authorizationHeader.startsWith("Bearer")) {
	 * jwt=authorizationHeader.substring(7); userName=util.extractUserName(jwt); }
	 * if(userName != null &&
	 * SecurityContextHolder.getContext().getAuthentication()== null) { User
	 * user=this.userService.loadUserByUserName(userName);
	 * if(util.validateToken(jwt, user)) { UsernamePasswordAuthenticationToken
	 * usernamePasswordAuthenticationToken= new
	 * UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
	 * usernamePasswordAuthenticationToken.setDetails(new
	 * WebAuthenticationDetailsSource().buildDetails(request));
	 * SecurityContextHolder.getContext().setAuthentication(
	 * usernamePasswordAuthenticationToken); } } }
	 */

}
