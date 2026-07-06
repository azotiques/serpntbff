package org.azotiques.serpnt_bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

@Configuration
public class SecurityConfig {
  private final String mainUri;

  public SecurityConfig(@Value("${app.main-uri}") String mainUri) {
    this.mainUri = mainUri;
  }

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, authException) -> {
      response.setStatus(401);
      response.setContentType("application/json");
      response.getWriter().print("{ \"error\": \"Unauthorized\" }");
    };
  }

  @Bean
  public SecurityFilterChain config(HttpSecurity http, AuthenticationEntryPoint entryPoint) throws Exception {

    http
        .oauth2Login(o -> o
            .defaultSuccessUrl(mainUri, true)
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/oauth2/authorization/**", "/login/oauth2/code/**").permitAll()
            .anyRequest().authenticated())
        .exceptionHandling(e -> e
            .defaultAuthenticationEntryPointFor(
            new LoginUrlAuthenticationEntryPoint("/oauth2/authorization/keycloak"),
            new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
            )
            .authenticationEntryPoint(entryPoint)
        )
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable);

    return http.build();
  }
}
