package ar.edu.itba.paw.webapp.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class PlatformUrlAuthenticationSuccessHandler 
	extends SimpleUrlAuthenticationSuccessHandler
	implements AuthenticationSuccessHandler {
	
	@Autowired
	private TokenAuthenticationManager tam;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		tam.authenticate(authentication, response);
		clearAuthenticationAttributes(request);
	}

}
