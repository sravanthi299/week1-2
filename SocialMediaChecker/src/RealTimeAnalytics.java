import java.util.*;

class Event {
    static void main() {

    }
    String url;
    String userId;
    String source;

    Event(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class RealTimeAnalytics {

    // pageUrl -> visit count
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique users
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // source -> visit count
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process page view event
    public void processEvent(Event e) {

        // Count page views
        pageViews.put(e.url, pageViews.getOrDefault(e.url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(e.url, new HashSet<>());
        uniqueVisitors.get(e.url).add(e.userId);

        // Track traffic source
        trafficSources.put(e.source,
                trafficSources.getOrDefault(e.source, 0) + 1);
    }

    // Get top 10 pages
    public void getDashboard() {

        System.out.println("Top Pages:");

        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        int limit = Math.min(10, list.size());

        for (int i = 0; i < limit; i++) {

            String url = list.get(i).getKey();
            int views = list.get(i).getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println((i + 1) + ". " + url +
                    " - " + views + " views (" + unique + " unique)");
        }

        System.out.println("\nTraffic Sources:");
        for (String source : trafficSources.keySet()) {
            System.out.println(source + " : " + trafficSources.get(source));
        }
    }

    public static void main(String[] args) {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent(new Event("/article/breaking-news","user_123","google"));
        analytics.processEvent(new Event("/article/breaking-news","user_456","facebook"));
        analytics.processEvent(new Event("/sports/championship","user_777","direct"));
        analytics.processEvent(new Event("/sports/championship","user_888","google"));
        analytics.processEvent(new Event("/sports/championship","user_777","google"));

        analytics.getDashboard();
    }
}