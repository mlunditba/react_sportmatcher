package ar.edu.itba.paw.webapp.config;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import ar.edu.itba.paw.webapp.auth.PlatformUrlAuthenticationSuccessHandler;
import ar.edu.itba.paw.webapp.auth.PlatformUserDetailsService;
import ar.edu.itba.paw.webapp.auth.StatelessFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private PlatformUserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private StatelessFilter sf;

    @Bean
    public DaoAuthenticationProvider getDaoAuth() {
        DaoAuthenticationProvider ap = new DaoAuthenticationProvider();
        ap.setUserDetailsService(userDetailsService);
        ap.setPasswordEncoder(passwordEncoder);
        return ap;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getDaoAuth());
    }
    
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
        	.addFilterBefore(corsFilter(), SecurityContextPersistenceFilter.class)
        	.userDetailsService(userDetailsService)
        		.authorizeRequests()
        			.antMatchers(HttpMethod.OPTIONS).permitAll()
	                .antMatchers(HttpMethod.POST, "/api/users").permitAll()
	                .antMatchers("/api/users/login").permitAll()
	                .antMatchers("/api/users/**").authenticated()
	                .antMatchers("/api/admin/**").access("hasRole('ROLE_ADMIN')")
	                .antMatchers(HttpMethod.POST, "/api/pitches/*/events/*/join", "/api/pitches/*/events/*/leave", "/api/pitches/*/events/*/upvote",
	                		"/api/pitches/*/events/*/downvote", "/api/pitches/*/events/*/kick-user/*")
	                		.access("hasRole('ROLE_USER')")
	                .antMatchers(HttpMethod.DELETE, "/api/pitches/*/events/*").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	                .antMatchers("/api/events/**").permitAll()
	                .antMatchers(HttpMethod.POST, "/api/clubs/*/comment").authenticated()
	                .antMatchers("/api/clubs/**").permitAll()
	                .antMatchers(HttpMethod.POST, "/api/pitches/*/events").access("hasRole('ROLE_USER')")
	                .antMatchers("/api/pitches/**").permitAll()
	                .antMatchers(HttpMethod.POST, "/api/tournaments/*/teams/*/join",
	                		"/api/tournaments/*/leave").access("hasRole('ROLE_USER')")
	                .antMatchers("/api/tournaments/**").permitAll()
	                .antMatchers("/","/index", "/api").permitAll()
	                // POST method restricted unless overridden above, like POST /users/login
	                .antMatchers(HttpMethod.POST).authenticated()
	                .antMatchers(HttpMethod.DELETE).authenticated()
                .and().sessionManagement()
                	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            .and().formLogin()
	                .usernameParameter("login_username")
	                .passwordParameter("login_password")
	                .loginProcessingUrl("/api/users/login")
	                .successHandler(platformAuthenticationSuccessHandler())
	                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and().logout().logoutUrl("/api/users/logout").logoutSuccessUrl("/")
                .and().exceptionHandling()
               		.authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .and().csrf()
                	.disable().addFilterBefore(sf, UsernamePasswordAuthenticationFilter.class);
    }
    
    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/favicon.ico", "/index.html", "/404.html", "/header.html",
                		"/sidebar.html", "/images/**", "/scripts/**",
                		"/styles/**", "/views/**", "/bower_components/**");
    }
    
    @Bean
    public AuthenticationSuccessHandler platformAuthenticationSuccessHandler(){
        return new PlatformUrlAuthenticationSuccessHandler();
    }
    
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public String tokenSecretKey() {
    	// TODO: read secret key from file
    	return Base64.getEncoder().encodeToString(
    			"E176EA76D237D19395AD4FBA2B605B82A9BCFB4F8ECBFE7E06C237889BB64EFA".getBytes());
    }

    private static final List<String> ALLOWED_ORIGINS = Collections.unmodifiableList(
    		Arrays.asList("*"));
    private static final List<String> ALLOWED_METHODS = Collections.unmodifiableList(
    		Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
    private static final List<String> ALLOWED_HEADERS = Collections.unmodifiableList(
    		Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-Auth-Token"));
    private static final List<String> EXPOSED_HEADERS = Collections.unmodifiableList(
    		Arrays.asList("X-Auth-Token"));

    @Bean
    public CorsFilter corsFilter() {
    	final CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ALLOWED_HEADERS);
        configuration.setExposedHeaders(EXPOSED_HEADERS);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);    	
    }

}
