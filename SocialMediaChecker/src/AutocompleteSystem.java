import java.util.*;

class TrieNode {
    HashMap<Character, TrieNode> children = new HashMap<>();
    HashMap<String, Integer> queries = new HashMap<>();
    boolean isEnd = false;
}

public class AutocompleteSystem {

    private TrieNode root = new TrieNode();

    // Insert query with frequency
    public void insert(String query, int frequency) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            node.queries.put(query,
                    node.queries.getOrDefault(query, 0) + frequency);
        }

        node.isEnd = true;
    }

    // Get top 10 suggestions for prefix
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }

            node = node.children.get(c);
        }

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(node.queries.entrySet());

        List<String> result = new ArrayList<>();

        int k = 10;
        while (!pq.isEmpty() && k-- > 0) {
            result.add(pq.poll().getKey());
        }

        return result;
    }

    // Update frequency when user searches
    public void updateFrequency(String query) {
        insert(query, 1);
    }

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.insert("java tutorial", 1234567);
        system.insert("javascript", 987654);
        system.insert("java download", 456789);
        system.insert("java 21 features", 1000);

        System.out.println("Suggestions for 'jav':");

        List<String> suggestions = system.search("jav");

        int rank = 1;
        for (String s : suggestions) {
            System.out.println(rank++ + ". " + s);
        }

        system.updateFrequency("java 21 features");
    }
}
