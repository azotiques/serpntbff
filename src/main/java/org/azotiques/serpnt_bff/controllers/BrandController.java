package org.azotiques.serpnt_bff.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;

import static org.springframework.security.oauth2.client.web.ClientAttributes.clientRegistrationId;

@RestController
public class BrandController {

  private final WebClient webClient;

  public BrandController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/brands")
  public JsonNode getBrands() {
    return webClient.get()
        .uri("/api/brands")
        .attributes(clientRegistrationId("keycloak"))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .block();
  }

  @PostMapping("/brands")
  public JsonNode createBrands(@RequestBody JsonNode brands) {
    return webClient.post()
        .uri("/api/brands")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(brands)
        .attributes(clientRegistrationId("keycloak"))
        .retrieve()
        .bodyToMono(JsonNode.class)
        .block();
  }

}
