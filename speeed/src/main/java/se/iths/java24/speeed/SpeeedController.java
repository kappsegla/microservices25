package se.iths.java24.speeed;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SpeeedController {

    private final PeakRpsTracker peakRpsTracker;
    private final UserService userService;

    public SpeeedController(PeakRpsTracker peakRpsTracker,UserService userService) {
        this.peakRpsTracker = peakRpsTracker;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(@RequestParam String name, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return userService.register(name, ip);
    }

    @GetMapping("/poke")
    public void poke(@RequestParam String token, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        // increment per-request counter
        peakRpsTracker.increment();

        userService.poke(token, ip);
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        Map<String, Object> base = userService.getStats();
        base.put("peakRps", peakRpsTracker.getPeakRps());
        return base;
    }
}
