package se.iths.java24.speeed;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PeakRpsTracker {

    private final AtomicInteger currentSecondCount = new AtomicInteger();
    private volatile int peakRps = 0;

    /**
     * Call this on every poke request.
     */
    public void increment() {
        currentSecondCount.incrementAndGet();
    }

    /**
     * Every second, reset the counter and update the peak if needed.
     */
    @Scheduled(fixedRate = 1_000)
    public void tick() {
        int count = currentSecondCount.getAndSet(0);
        peakRps = Math.max(peakRps, count);
    }

    public int getPeakRps() {
        return peakRps;
    }
}
