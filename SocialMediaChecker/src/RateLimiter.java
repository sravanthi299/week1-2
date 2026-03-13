import java.util.*;

class TokenBucket {
    static void main() {

    }
    int tokens;
    int maxTokens;
    double refillRate; // tokens per second
    long lastRefillTime;

    TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // refill tokens based on time passed
    synchronized void refill() {
        long now = System.currentTimeMillis();
        double tokensToAdd = (now - lastRefillTime) / 1000.0 * refillRate;

        if (tokensToAdd > 0) {
            tokens = Math.min(maxTokens, tokens + (int) tokensToAdd);
            lastRefillTime = now;
        }
    }

    // try to consume token
    synchronized boolean allowRequest() {
        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    int getRemainingTokens() {
        return tokens;
    }
}

public class RateLimiter {

    // clientId -> token bucket
    private HashMap<String, TokenBucket> clients = new HashMap<>();

    private int maxRequests = 1000;
    private double refillRate = 1000.0 / 3600.0; // 1000 per hour

    public boolean checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId,
                new TokenBucket(maxRequests, refillRate));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            System.out.println("Allowed (" +
                    bucket.getRemainingTokens() + " requests remaining)");
            return true;
        } else {
            System.out.println("Denied (Rate limit exceeded)");
            return false;
        }
    }

    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("No requests made yet.");
            return;
        }

        int used = maxRequests - bucket.getRemainingTokens();

        System.out.println("{used: " + used +
                ", limit: " + maxRequests +
                ", remaining: " + bucket.getRemainingTokens() + "}");
    }

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        String client = "abc123";

        for (int i = 0; i < 5; i++) {
            limiter.checkRateLimit(client);
        }

        limiter.getRateLimitStatus(client);
    }
}
