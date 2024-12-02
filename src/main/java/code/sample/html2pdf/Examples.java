package code.sample.html2pdf;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class Examples {

    public static void main(String[] args) {
        WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/api")
            .defaultHeader("Client-Id", "WebClient")
            .build();

//        String response = webClient.post()
//            .uri("/sync")
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Custom-Header", "customHeaderValue")
//            .bodyValue("{\"data\":\"value\"}")
//            .retrieve()
//            .bodyToMono(String.class)
//            .block();  // Synchronous call
//
//        System.out.println("Response: " + response);

        Mono<String> responseMono = webClient.post()
            .uri("/async")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Custom-Header", "customHeaderValue")
            .bodyValue("{\"data\":\"async idiots\"}")
            .retrieve()
            .bodyToMono(String.class);

        // Asynchronous processing
        responseMono.subscribe(r -> {
            System.out.println("Async Response: " + r);
        });

        // Sleep to ensure the async call completes before the program exits
        try {
            Thread.sleep(3000);  // This is only for demo purposes, not recommended in production.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
