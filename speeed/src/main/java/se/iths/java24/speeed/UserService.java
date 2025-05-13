package se.iths.java24.speeed;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GlobalStatsRepository globalStatsRepository;
    private final NamedParameterJdbcTemplate jdbc;

    public UserService(UserRepository userRepository, GlobalStatsRepository globalStatsRepository, NamedParameterJdbcTemplate jdbc) {
        this.userRepository = userRepository;
        this.globalStatsRepository = globalStatsRepository;
        this.jdbc = jdbc;
    }

    public String register(String name, String ip) {
        Optional<User> existing = userRepository.findByName(name);
        if (existing.isPresent()) {
            User user = existing.get();
            if (user.getIp().equals(ip)) {
                return user.getToken(); // Return existing token
            } else {
                throw new RuntimeException("Username already used by another IP");
            }
        }

        String token = UUID.randomUUID().toString();
        User newUser = new User();
        newUser.setName(name);
        newUser.setIp(ip);
        newUser.setToken(token);
        newUser.setCount(0);
        userRepository.save(newUser);

        return token;
    }

//    @Transactional
//    public void poke(String token, String ip) {
//        // 1. fetch & lock global row
//        GlobalStats stats = globalStatsRepository.findById(1L)
//                .orElseThrow(() -> new RuntimeException("Global stats not found"));
//        stats.setTotalCount(stats.getTotalCount() + 1);
//        globalStatsRepository.save(stats);
//
//        // 2. now fetch & lock user row
//        User user = userRepository.findByToken(token)
//                .orElseThrow(() -> new RuntimeException("Invalid token"));
//        if (! user.getIp().equals(ip)) throw new RuntimeException("IP address mismatch");
//        user.setCount(user.getCount() + 1);
//        userRepository.save(user);
//    }

    //Optimistic Locking
//    @Retryable(value = OptimisticLockingFailureException.class,
//            maxAttempts = 5, backoff = @Backoff(delay = 50))
//    @Transactional
//    public void poke(String token, String ip) {
//        // fetch+save GlobalStats
//        GlobalStats stats = globalStatsRepository.findById(1L)
//                .orElseThrow();
//        stats.setTotalCount(stats.getTotalCount() + 1);
//        globalStatsRepository.save(stats);
//
//        // fetch+save User
//        User user = userRepository.findByToken(token)
//                .orElseThrow();
//        if (!user.getIp().equals(ip)) throw new RuntimeException("IP mismatch");
//        user.setCount(user.getCount() + 1);
//        userRepository.save(user);
//    }


    //Pessimistic Locking with SELECT … FOR UPDATE
    @Transactional
    public void poke(String token, String ip) {
        // 1) LOCK global_stats row
        jdbc.queryForObject(
                "SELECT total_count FROM global_stats WHERE id = :id FOR UPDATE",
                Map.of("id", 1L),
                Integer.class
        );
        // now safe to update
        jdbc.update(
                "UPDATE global_stats SET total_count = total_count + 1 WHERE id = :id",
                Map.of("id", 1L)
        );

        // 2) LOCK user row
        Long userId = jdbc.queryForObject(
                "SELECT id FROM users WHERE token = :token FOR UPDATE",
                Map.of("token", token),
                Long.class
        );
        // update user count
        jdbc.update(
                "UPDATE users SET count = count + 1 WHERE id = :id",
                Map.of("id", userId)
        );
    }




    public Map<String, Object> getStats() {
        GlobalStats stats = globalStatsRepository.findById(1L).orElse(new GlobalStats());
        Iterable<User> users = userRepository.findAll();

        List<Map<String, Object>> userList = new ArrayList<>();
        int sum = 0;

        for (User user : users) {
            Map<String, Object> u = new HashMap<>();
            u.put("name", user.getName());
            u.put("ip", user.getIp());
            u.put("count", user.getCount());
            sum += user.getCount();
            userList.add(u);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", stats.getTotalCount());
        result.put("users", userList);
        result.put("consistent", stats.getTotalCount() == sum);

        return result;
    }
}
