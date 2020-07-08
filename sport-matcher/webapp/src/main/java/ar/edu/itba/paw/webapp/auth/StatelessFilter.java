package ar.edu.itba.paw.webapp.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class StatelessFilter extends GenericFilterBean {
	
	@Autowired
	private TokenAuthenticationManager tam;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Authentication authentication = tam.getAuthentication(httpRequest);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
		SecurityContextHolder.getContext().setAuthentication(null);
	}

}
