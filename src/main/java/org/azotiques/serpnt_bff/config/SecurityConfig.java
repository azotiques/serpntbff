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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
  private final String mainUri;
  private final List<String> allowedOriginPatterns;

  public SecurityConfig(@Value("${app.main-uri}") String mainUri,
                        @Value("${app.cors.allowed-origin-patterns}") String allowedOriginPatterns) {
    this.mainUri = mainUri;
    this.allowedOriginPatterns = Arrays.stream(allowedOriginPatterns.split(","))
        .map(String::trim)
        .filter(origin -> !origin.isBlank())
        .toList();
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
        .cors(c -> {
          c.configurationSource(corsConfigurationSource());
        });

    return http.build();
  }

  @Bean
  UrlBasedCorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(allowedOriginPatterns);
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(Arrays.asList(
        "Authorization", "Content-Type", "Accept", "X-Requested-With"
    ));
    configuration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
