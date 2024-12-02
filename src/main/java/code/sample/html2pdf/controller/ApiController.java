package code.sample.html2pdf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    private final Environment environment;

    @EventListener(ContextRefreshedEvent.class)
    public void displayServerAddress() throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = environment.getProperty("server.port");
        System.out.println("Application is running at http://" + ip + ":" + port);
    }

    // Synchronous API - This API simulates a data processing task
    @PostMapping("/sync")
    public ResponseEntity<String> syncApi(@RequestBody Map<String, Object> requestBody,
                                          @RequestHeader HttpHeaders headers) {
        String clientId = headers.getFirst("Client-Id");
        String requestData = requestBody.get("data").toString();

        // Simulate some complex logic
        String result = String.format("Processed data '%s' from client '%s'", requestData, clientId);
        return ResponseEntity.ok(result);
    }

    // Asynchronous API - This API simulates a task that takes longer to process
    @PostMapping("/async")
    public CompletableFuture<ResponseEntity<String>> asyncApi(@RequestBody Map<String, Object> requestBody,
                                                              @RequestHeader HttpHeaders headers) {
        return CompletableFuture.supplyAsync(() -> {
            String clientId = headers.getFirst("Client-Id");
            String requestData = requestBody.get("data").toString();

            // Simulate some delay for a complex task
            try {
                Thread.sleep(3000); // Simulating long processing time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            String result = String.format("Processed data '%s' asynchronously from client '%s'", requestData, clientId);
            return ResponseEntity.ok(result);
        });
    }
}
