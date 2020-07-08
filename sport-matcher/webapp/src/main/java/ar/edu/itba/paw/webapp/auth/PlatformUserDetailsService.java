package ar.edu.itba.paw.webapp.auth;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;

@Component
public class PlatformUserDetailsService implements UserDetailsService {
	
	@Qualifier("userServiceImpl")
	@Autowired
    private UserService us;
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final Optional<User> user = us.findByUsername(username);

        if(!user.isPresent()) {
            throw new UsernameNotFoundException("No username found by the name " + username);
        }
        
        final Collection<? extends GrantedAuthority> authorities;
        if(user.get().getRole().equals(Role.ROLE_ADMIN)) {
	        authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        else if(user.get().getRole().equals(Role.ROLE_USER)) {
        	authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        else
        	throw new IllegalStateException();

        return new org.springframework.security.core.userdetails
                .User(username, user.get().getPassword(), authorities);
    }

}
