package se.iths.java24.bff;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class ResourceController {

    private final OAuth2AuthorizedClientService clientService;
    private final WebClient webClient;

    public ResourceController(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
        this.webClient = WebClient.builder().build();
    }

    @GetMapping("/api/proxy")
    public Mono<String> proxyResource(@AuthenticationPrincipal OAuth2User principal,
                                      @RegisteredOAuth2AuthorizedClient("my-auth-server") OAuth2AuthorizedClient client) {
        return webClient.get()
                .uri("http://localhost:8080/secure")
                .headers(h -> h.setBearerAuth(client.getAccessToken().getTokenValue()))
                .retrieve()
                .bodyToMono(String.class);
    }
}
