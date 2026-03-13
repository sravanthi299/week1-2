import java.util.*;

class DNSEntry {
    static void main() {

    }
    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, long ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCache {

    private int capacity;
    private LinkedHashMap<String, DNSEntry> cache;

    private int hits = 0;
    private int misses = 0;

    public DNSCache(int capacity) {
        this.capacity = capacity;

        // accessOrder=true enables LRU behavior
        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.capacity;
            }
        };
    }

    // Simulate upstream DNS lookup
    private String queryUpstreamDNS(String domain) {
        return "192.168." + new Random().nextInt(255) + "." + new Random().nextInt(255);
    }

    public String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            System.out.println("Cache HIT");
            return entry.ipAddress;
        }

        if (entry != null && entry.isExpired()) {
            System.out.println("Cache EXPIRED");
            cache.remove(domain);
        }

        misses++;
        System.out.println("Cache MISS → Query upstream");

        String ip = queryUpstreamDNS(domain);

        cache.put(domain, new DNSEntry(domain, ip, 5)); // TTL = 5 seconds (demo)

        return ip;
    }

    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = (total == 0) ? 0 : (hits * 100.0) / total;

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) throws InterruptedException {

        DNSCache dnsCache = new DNSCache(5);

        System.out.println("IP: " + dnsCache.resolve("google.com"));
        System.out.println("IP: " + dnsCache.resolve("google.com"));

        Thread.sleep(6000); // wait for TTL expiry

        System.out.println("IP: " + dnsCache.resolve("google.com"));

        dnsCache.getCacheStats();
    }
}
