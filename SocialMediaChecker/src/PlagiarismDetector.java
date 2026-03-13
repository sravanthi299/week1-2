import java.util.*;

public class PlagiarismDetector {
    static void main() {

    }

    // n-gram -> set of document IDs
    private HashMap<String, Set<String>> index = new HashMap<>();

    private int n = 5; // 5-gram

    // Extract n-grams from a document
    private List<String> getNGrams(String text) {

        List<String> ngrams = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - n; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < n; j++) {
                gram.append(words[i + j]).append(" ");
            }

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }

    // Add document to database
    public void addDocument(String docId, String text) {

        List<String> ngrams = getNGrams(text);

        for (String gram : ngrams) {

            index.putIfAbsent(gram, new HashSet<>());
            index.get(gram).add(docId);
        }
    }

    // Analyze new document
    public void analyzeDocument(String docId, String text) {

        List<String> ngrams = getNGrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (index.containsKey(gram)) {

                for (String existingDoc : index.get(gram)) {

                    matchCount.put(existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with " + doc);

            System.out.println("Similarity: " + similarity + "%");

            if (similarity > 50) {
                System.out.println("PLAGIARISM DETECTED with " + doc);
            }
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 = "Artificial intelligence is transforming modern technology and education systems";
        String essay2 = "Artificial intelligence is transforming modern technology and healthcare systems";

        detector.addDocument("essay_089.txt", essay1);

        detector.analyzeDocument("essay_123.txt", essay2);
    }
}