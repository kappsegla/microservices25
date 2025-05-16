package se.iths.java24.resourceserver;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/secure")
    public String secureEndpoint() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return "Hello from the SECURE Resource Server! Token is valid. Server ip: " + ip.getHostAddress();
        } catch (UnknownHostException e) {
            return "Hello from the SECURE Resource Server! Token is valid.";
        }
    }

    @GetMapping("/public")
    public String publicEndpoint(HttpServletRequest request) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return "Hello from the PUBLIC Resource Server! Server ip: " + ip.getHostAddress();
        } catch (UnknownHostException e) {
            return "Hello from the PUBLIC Resource Server!";
        }
    }
}
