package org.azotiques.serpnt_bff.controllers;

import org.azotiques.serpnt_bff.dtos.CartProductsDto;
import org.azotiques.serpnt_bff.dtos.CartResponseDto;
import org.azotiques.serpnt_bff.dtos.ProductDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.oauth2.client.web.ClientAttributes.clientRegistrationId;

@RestController
public class CartController {

  private final WebClient webClient;

  public CartController(WebClient webClient) {
    this.webClient = webClient;
  }

  @PostMapping("/carts")
  public JsonNode addToCart(@RequestParam Long productId) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder
            .path("/api/carts")
            .queryParam("productId", productId)
            .build())
        .attributes(clientRegistrationId("keycloak"))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .block();
  }

  @GetMapping("/carts")
  public List<CartProductsDto> getCartProducts() {

    List<CartResponseDto> cartResponse = webClient.get()
        .uri("/api/carts")
        .attributes(clientRegistrationId("keycloak"))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<CartResponseDto>>() {})
        .block();

    List<CartProductsDto> cartProductsDtos = new ArrayList<>();

    for(CartResponseDto cartResponseDto : cartResponse) {
      ProductDto productDto = webClient.get()
          .uri("/api/products/" + cartResponseDto.productId())
          .attributes(clientRegistrationId("keycloak"))
          .retrieve()
          .bodyToMono(ProductDto.class)
          .block();

      CartProductsDto cartProductsDto = new CartProductsDto(cartResponseDto.quantity(), productDto);
      cartProductsDtos.add(cartProductsDto);
    }

    return cartProductsDtos;
  }
}
