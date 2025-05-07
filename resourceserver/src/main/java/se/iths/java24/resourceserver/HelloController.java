package se.iths.java24.resourceserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/secure")
    public String secureEndpoint() {
        return "Hello from the SECURE Resource Server! Token is valid.";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Hello from the PUBLIC Resource Server!";
    }
}
