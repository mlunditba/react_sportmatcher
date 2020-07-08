package ar.edu.itba.paw.webapp.auth;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Component
public class TokenAuthenticationManager {

	private static final String AUTH_HEADER = "X-Auth-Token";
	
	@Autowired
	private String tokenSecretKey;
	
	@Autowired
	private UserDetailsService uds;
	
//	public Authentication getAuthentication() {
//		return SecurityContextHolder.getContext().getAuthentication();
//	}
//	
//	public boolean isAuthenticated() {
//		Authentication auth = getAuthentication();
//		return auth != null && auth.isAuthenticated() && 
//				!(auth instanceof AnonymousAuthenticationToken);
//	}
//	
//	public boolean isAdmin() {
//		Authentication auth = getAuthentication();
//		if(!isAuthenticated())
//			return false;
//		for(GrantedAuthority authority : auth.getAuthorities()) {
//			if(authority.getAuthority().equals("ROLE_ADMIN"))
//				return true;
//		}
//		return false;
//	}
	
	public void authenticate(Authentication authentication, HttpServletResponse response) {
		final String token = generateTokenForUser(authentication.getName());
		response.addHeader(AUTH_HEADER, token);
	}
	
	public Authentication getAuthentication(final HttpServletRequest request) {
		String token = request.getHeader(AUTH_HEADER);
		if(isPresent(token)) {
			UserDetails u = getUserFromToken(token);
			if(u != null)
				return new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword());
		}
		return null;
	}
	
	private boolean isPresent(final String token) {
		return token != null && Jwts.parser().isSigned(token);
	}
	
//	public void authenticate(String username, String password, HttpServletRequest request) {
////		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
////				username, password);
////		authToken.setDetails(new WebAuthenticationDetails(request));
////		Authentication authentication = authenticationManager.authenticate(authToken);
//		Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//	}
	
	private String generateTokenForUser(final String username) {
		return Jwts.builder()
				.setId(UUID.randomUUID().toString()).setSubject(username).signWith(SignatureAlgorithm.HS512, tokenSecretKey)
				.compact();
	}
	
	private UserDetails getUserFromToken(final String token) {
		try {
			final String username = Jwts.parser().setSigningKey(tokenSecretKey)
					.parseClaimsJws(token).getBody().getSubject();
			return uds.loadUserByUsername(username);
		} catch (SignatureException e) {
			return null;
		}
	}

}
