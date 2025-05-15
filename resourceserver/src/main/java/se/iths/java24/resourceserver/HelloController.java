package se.iths.java24.resourceserver;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/secure")
    public String secureEndpoint() {
        return "Hello from the SECURE Resource Server! Token is valid.";
    }

    @GetMapping("/public")
    public String publicEndpoint(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        int clientPort = request.getRemotePort();
        List<String> headerNames = Collections.list(request.getHeaderNames());

        logger.info("Client IP: {} - Port: {}", clientIp, clientPort);
        logger.info("Forwarded-For: {}", request.getRemoteHost());
        logger.info("Forwarded-Port: {}", request.getRemotePort());

        return "Hello from the PUBLIC Resource Server!";
    }
}
