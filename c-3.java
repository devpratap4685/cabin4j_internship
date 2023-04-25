import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Autocomplete {

    private static final int K = 5; // maximum number of suggestions to return
    private static final String FILENAME = "input.txt"; // input file name
    
    private static class Node {
        Map<Character, Node> children;
        double weight;
        
        Node() {
            children = new HashMap<>();
            weight = 0;
        }
    }
    
    private static class WeightedString implements Comparable<WeightedString> {
        String str;
        double weight;
        
        WeightedString(String str, double weight) {
            this.str = str;
            this.weight = weight;
        }
        
        @Override
        public int compareTo(WeightedString other) {
            return Double.compare(other.weight, weight); // sort in descending order of weight
        }
    }
    
    private static class Trie {
        Node root;
        
        Trie() {
            root = new Node();
        }
        
        void addString(String str, double weight) {
            Node node = root;
            for (char c : str.toCharArray()) {
                if (!node.children.containsKey(c)) {
                    node.children.put(c, new Node());
                }
                
                node = node.children.get(c);
            }
            
            node.weight = weight;
        }
        
        List<WeightedString> getTopSuggestions(String prefix, int k) {
            Node node = root;
            for (char c : prefix.toCharArray()) {
                if (!node.children.containsKey(c)) {
                    return Collections.emptyList(); // prefix not found in trie
                }
                
                node = node.children.get(c);
            }
            
            PriorityQueue<WeightedString> suggestions = new PriorityQueue<>();
            dfs(node, prefix, suggestions, k);
            List<WeightedString> topSuggestions = new ArrayList<>(suggestions);
            Collections.sort(topSuggestions);
            return topSuggestions;
        }
        
        void dfs(Node node, String prefix, PriorityQueue<WeightedString> suggestions, int k) {
            if (node.weight > 0) {
                suggestions.offer(new WeightedString(prefix, node.weight));
                if (suggestions.size() > k) {
                    suggestions.poll();
                }
            }
            
            for (char c : node.children.keySet()) {
                dfs(node.children.get(c), prefix + c, suggestions, k);
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        Trie trie = new Trie();
        
        BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\s+");
            String str = parts[1];
            double weight = Double.parseDouble(parts[0]);
            trie.addString(str, weight);
        }
        reader.close();
        
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter prefix (or q to quit): ");
            String prefix = scanner.nextLine();
            if (prefix.equals("q")) {
                break;
            }
            
            List<WeightedString> suggestions = trie.getTopSuggestions(prefix, K);
            if (suggestions.isEmpty()) {
                System.out.println("No suggestions found.");
            } else {
                System.out.println("Top " + K + " suggestions:");
                for (WeightedString suggestion : suggestions) {
                    System.out.println(suggestion.str + " (" + suggestion.weight + ")");
                }
            }
        }
        scanner.close();
    }
}
