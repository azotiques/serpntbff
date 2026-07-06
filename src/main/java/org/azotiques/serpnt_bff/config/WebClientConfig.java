package org.azotiques.serpnt_bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
  private final String backendApiDomain;

  public WebClientConfig(@Value("${app.backend-api-domain}") String backendApiDomain) {
    this.backendApiDomain = backendApiDomain;
  }

  @Bean
  WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
    ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
        new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

    oauth2.setDefaultClientRegistrationId("keycloak");

    return WebClient.builder()
        .baseUrl(backendApiDomain)
        .filter(oauth2)
        .build();
  }

  @Bean
  public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository repo,
                                                               OAuth2AuthorizedClientRepository service) {
    OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder()
        .authorizationCode()
        .refreshToken()
        .build();

    DefaultOAuth2AuthorizedClientManager manager = new  DefaultOAuth2AuthorizedClientManager(repo, service);

    manager.setAuthorizedClientProvider(provider);

    return manager;
  }
}
