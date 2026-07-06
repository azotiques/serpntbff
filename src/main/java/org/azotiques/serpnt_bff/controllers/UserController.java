package org.azotiques.serpnt_bff.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;

import static org.springframework.security.oauth2.client.web.ClientAttributes.clientRegistrationId;

@RestController
public class UserController {

  private final WebClient webClient;

  public UserController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/me")
  public JsonNode test() {
    return webClient.get()
        .uri("/api/me")
        .attributes(clientRegistrationId("keycloak"))
        .retrieve()
        .bodyToMono(JsonNode.class)
        .block();
  }
}
