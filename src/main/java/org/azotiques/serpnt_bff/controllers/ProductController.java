package org.azotiques.serpnt_bff.controllers;

import org.azotiques.serpnt_bff.dtos.ProductDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;

import static org.springframework.security.oauth2.client.web.ClientAttributes.clientRegistrationId;

@RestController
public class ProductController{

  private final WebClient webClient;

  public ProductController(WebClient webClient) {
    this.webClient = webClient;
  }

  @PostMapping("/products/filter")
  public JsonNode getProductsByFilter(@RequestBody String name) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder
            .path("/api/products/filter")
            .build())
        .attributes(clientRegistrationId("keycloak"))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(name)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .block();
  }

  @GetMapping("/products")
  public JsonNode getProducts() {
    return webClient.get()
        .uri("/api/products")
        .attributes(clientRegistrationId("keycloak"))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .block();
  }

  @PostMapping("/products")
  public JsonNode createProducts(@RequestBody JsonNode products) {
    return webClient.post()
        .uri("/api/products")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(products)
        .attributes(clientRegistrationId("keycloak"))
        .retrieve()
        .bodyToMono(JsonNode.class)
        .block();
  }

}
