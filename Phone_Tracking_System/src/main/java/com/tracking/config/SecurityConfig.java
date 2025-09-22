package com.tracking.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.tracking.service.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserDetailsServiceImpl userDetailsService;

	public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**", "/api/public/**").permitAll()
						.requestMatchers("/api/admin/**").hasRole("ADMIN").anyRequest().authenticated())
				.formLogin(form -> form.loginProcessingUrl("/api/auth/login")
						.successHandler(authenticationSuccessHandler()).failureHandler(authenticationFailureHandler())
						.permitAll())
				.logout(logout -> logout.logoutUrl("/api/auth/logout")
						.logoutSuccessHandler((request, response, authentication) -> {
							response.setStatus(HttpServletResponse.SC_OK);
							response.getWriter().write("{\"message\":\"Logout successful\"}");
							response.setContentType("application/json");
						}).invalidateHttpSession(true).deleteCookies("JSESSIONID"))
				.sessionManagement(session -> session.maximumSessions(1).maxSessionsPreventsLogin(false));

		return http.build();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return (request, response, authentication) -> {
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter()
					.write("{\"message\":\"Login successful\",\"user\":\"" + authentication.getName() + "\"}");
			response.setContentType("application/json");
		};
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return (request, response, exception) -> {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"error\":\"Invalid credentials\"}");
			response.setContentType("application/json");
		};
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
