import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    long time;

    Transaction(int id, int amount, String merchant, String account, long time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class TransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction other = map.get(complement);

                System.out.println("Two-Sum Pair: (" +
                        other.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    // Two-Sum within 1 hour window
    public void findTwoSumTimeWindow(int target, long windowMillis) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                if (Math.abs(t.time - other.time) <= windowMillis) {
                    System.out.println("Time-window pair: (" +
                            other.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }

    // Duplicate detection
    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {
                System.out.println("Duplicate transactions: " + key);

                for (Transaction t : list) {
                    System.out.println("ID: " + t.id +
                            " Account: " + t.account);
                }
            }
        }
    }

    // Simple 3-Sum example
    public void findThreeSum(int target) {

        int n = transactions.size();

        for (int i = 0; i < n; i++) {

            HashSet<Integer> set = new HashSet<>();
            int newTarget = target - transactions.get(i).amount;

            for (int j = i + 1; j < n; j++) {

                int complement = newTarget - transactions.get(j).amount;

                if (set.contains(complement)) {

                    System.out.println("3-Sum: (" +
                            transactions.get(i).id + ", " +
                            transactions.get(j).id + ")");
                }

                set.add(transactions.get(j).amount);
            }
        }
    }

    public static void main(String[] args) {

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        long now = System.currentTimeMillis();

        analyzer.addTransaction(new Transaction(1, 500, "StoreA", "acc1", now));
        analyzer.addTransaction(new Transaction(2, 300, "StoreB", "acc2", now + 5000));
        analyzer.addTransaction(new Transaction(3, 200, "StoreC", "acc3", now + 10000));
        analyzer.addTransaction(new Transaction(4, 500, "StoreA", "acc4", now + 15000));

        analyzer.findTwoSum(500);

        analyzer.findTwoSumTimeWindow(500, 3600000);

        analyzer.detectDuplicates();

        analyzer.findThreeSum(1000);
    }
}