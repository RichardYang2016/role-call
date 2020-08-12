package com.google.rolecall.config;

import com.google.rolecall.authentication.PreAuthTokenHeaderFilter;
import com.google.rolecall.authentication.SameSiteFilter;

import java.util.Arrays;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/* Security Configuration for authenticating a request to the REST application. */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final AuthenticationProvider authProvider;
  private final Environment env;

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
      authenticationManagerBuilder.authenticationProvider(authProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic()
        .and()
        .cors()
        .and().addFilter(getFilter())
        .addFilterAfter(getSameSite(), BasicAuthenticationFilter.class)
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        .sessionFixation().migrateSession()
        .and().authorizeRequests().antMatchers("/api/**").authenticated()
        .and().logout()
        .deleteCookies("SESSIONID").invalidateHttpSession(true)
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
        .permitAll()
        .and()
        .csrf().disable();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    String allowedOrigin = env.getProperty("rolecall.frontend.url");
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(Arrays.asList(allowedOrigin));
    configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH","DELETE"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(Arrays.asList("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }

  /** Initializes the filter for Authentication header information. */
  private PreAuthTokenHeaderFilter getFilter() throws Exception {
    PreAuthTokenHeaderFilter filter = new PreAuthTokenHeaderFilter();
    filter.setAuthenticationManager(authenticationManager());
    return filter;
  }

  private Filter getSameSite() throws Exception {
    Filter filter = new SameSiteFilter();
    return filter;
  }

  public WebSecurityConfig(AuthenticationProvider authProvider, Environment env) {
    this.authProvider = authProvider;
    this.env = env;
  }
}
