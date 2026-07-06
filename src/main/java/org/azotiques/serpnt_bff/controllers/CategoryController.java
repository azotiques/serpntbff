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
public class CategoryController{

  private final WebClient webClient;

  public CategoryController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/categories")
  public JsonNode getCategories() {
    return webClient.get()
        .uri("/api/categories")
        .attributes(clientRegistrationId("keycloak"))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .block();
  }

  @PostMapping("/categories")
  public JsonNode createCategories(@RequestBody JsonNode categories) {
    return webClient.post()
        .uri("/api/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(categories)
        .attributes(clientRegistrationId("keycloak"))
        .retrieve()
        .bodyToMono(JsonNode.class)
        .block();
  }

}
