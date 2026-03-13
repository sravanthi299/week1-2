import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String id, String content) {
        this.videoId = id;
        this.content = content;
    }
}

public class MultiLevelCache {

    // L1 Cache (Memory)
    private LinkedHashMap<String, VideoData> L1;

    // L2 Cache (SSD simulation)
    private HashMap<String, VideoData> L2;

    // L3 Cache (Database simulation)
    private HashMap<String, VideoData> L3;

    private int L1_CAPACITY = 10000;
    private int L2_CAPACITY = 100000;

    private int L1_hits = 0, L2_hits = 0, L3_hits = 0;

    public MultiLevelCache() {

        L1 = new LinkedHashMap<String, VideoData>(L1_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1_CAPACITY;
            }
        };

        L2 = new HashMap<>();
        L3 = new HashMap<>();

        // Simulate database videos
        for (int i = 1; i <= 1000; i++) {
            L3.put("video_" + i, new VideoData("video_" + i, "VideoContent" + i));
        }
    }

    public VideoData getVideo(String videoId) {

        // L1 Cache check
        if (L1.containsKey(videoId)) {
            L1_hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 Cache check
        if (L2.containsKey(videoId)) {
            L2_hits++;
            System.out.println("L2 Cache HIT (5ms)");

            VideoData video = L2.get(videoId);
            L1.put(videoId, video); // promote to L1

            return video;
        }

        System.out.println("L2 Cache MISS");

        // L3 Database
        if (L3.containsKey(videoId)) {
            L3_hits++;
            System.out.println("L3 Database HIT (150ms)");

            VideoData video = L3.get(videoId);

            // Add to L2 cache
            if (L2.size() < L2_CAPACITY) {
                L2.put(videoId, video);
            }

            return video;
        }

        System.out.println("Video not found");
        return null;
    }

    public void getStatistics() {

        int total = L1_hits + L2_hits + L3_hits;

        double L1_rate = total == 0 ? 0 : (L1_hits * 100.0) / total;
        double L2_rate = total == 0 ? 0 : (L2_hits * 100.0) / total;
        double L3_rate = total == 0 ? 0 : (L3_hits * 100.0) / total;

        System.out.println("\nCache Statistics:");
        System.out.println("L1 Hit Rate: " + L1_rate + "%");
        System.out.println("L2 Hit Rate: " + L2_rate + "%");
        System.out.println("L3 Hit Rate: " + L3_rate + "%");
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_999");

        cache.getStatistics();
    }
}