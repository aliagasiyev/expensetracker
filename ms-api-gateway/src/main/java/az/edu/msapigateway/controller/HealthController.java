package az.edu.msapigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthController {

    @GetMapping("/healtzh")
    public Mono<String> health() {
        return Mono.just("Gateway is up and running!");
    }
    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("Gateway is up and running!");
    }
}