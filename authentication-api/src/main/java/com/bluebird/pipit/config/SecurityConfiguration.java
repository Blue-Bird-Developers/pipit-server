package com.bluebird.pipit.config;

import com.bluebird.pipit.security.CustomUserDetailsService;
import com.bluebird.pipit.security.JwtAuthenticationEntryPoint;
import com.bluebird.pipit.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
	securedEnabled = true,
	jsr250Enabled = true,
	prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	public SecurityConfiguration(CustomUserDetailsService customUserDetailsService,
								 JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
		this.customUserDetailsService = customUserDetailsService;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder
			.userDetailsService(customUserDetailsService)
			.passwordEncoder(passwordEncoder());
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/v2/api-docs", "/swagger-ui/index.html", "/swagger-ui/**",
			"/swagger-resources/**",
			"/swagger-ui/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.httpBasic()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.and()
			.authorizeRequests()
//			.antMatchers("/",
//				"/error",
//				"/favicon.ico",
//				"/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js",
//				"/swagger-resources/**", "/swagger-ui/**", "/swagger-resources")
//			.permitAll()
			.antMatchers("/user/id/find", "/user/login", "/user/logout", "/user/password/check",
				"/user/password/reset", "/user/portal", "/user/signup")
			.permitAll()
			.anyRequest()
			.authenticated();

		// Custom Token을 기본 authentication Filter 앞에 위치시킴
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}
