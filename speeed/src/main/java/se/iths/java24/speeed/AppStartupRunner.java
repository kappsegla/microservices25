package se.iths.java24.speeed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner {

    private final GlobalStatsRepository globalStatsRepository;

    public AppStartupRunner(GlobalStatsRepository globalStatsRepository) {
        this.globalStatsRepository = globalStatsRepository;
    }

    @Override
    public void run(String... args) {
        globalStatsRepository.findById(1L).orElseGet(() -> {
            GlobalStats stats = new GlobalStats();
            stats.setId(1L);
            stats.setTotalCount(0);
            return globalStatsRepository.save(stats);
        });
    }
}
