package org.azotiques.serpnt_bff.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;

import static org.springframework.security.oauth2.client.web.ClientAttributes.clientRegistrationId;

@RestController
@RequestMapping("/payments")
public class PaymentController {

  private final WebClient webClient;

  public PaymentController(WebClient webClient) {
    this.webClient = webClient;
  }

  @PostMapping("/create-intent")
  public JsonNode createIntent() {
    return webClient.post()
        .uri("/api/payments/create-intent")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .attributes(clientRegistrationId("keycloak"))
        .retrieve()
        .bodyToMono(JsonNode.class)
        .block();
  }

}
