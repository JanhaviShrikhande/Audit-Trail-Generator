package com.miniProject.AuditTrailGenerator.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DiffService {
    // removes punctuation but keeps apostrophes/dashes inside words
    private static final Pattern NON_WORD = Pattern.compile("[^\\w\\s'-]+");
    // simple set of English stop words (extend as needed)
    private static final Set<String> STOP_WORDS = Set.of(
            "a","an","the","and","or","but","if","then","is","are","was","were",
            "this","that","these","those","it","its","has","have","had","i","you",
            "he","she","they","we","of","in","on","for","to","with","as","by","at","from"
    );

    // Normalize and tokenize: lowercase, remove punctuation, trim, remove stopwords
    public List<String> tokenize(String text) {
        if (text == null || text.trim().isEmpty()) return Collections.emptyList();
        String cleaned = NON_WORD.matcher(text.toLowerCase()).replaceAll(" ");
        String[] parts = cleaned.split("\\s+");
        List<String> tokens = new ArrayList<>();
        for (String p : parts) {
            String t = p.trim();
            if (t.isEmpty()) continue;
            // remove leading/trailing apostrophe/dash leftover
            t = t.replaceAll("^[\\-']+|[\\-']+$", "");
            if (t.isEmpty()) continue;
            if (STOP_WORDS.contains(t)) continue; // skip stop words
            tokens.add(t);
        }
        return tokens;
    }

    // returns map with "added" and "removed" lists (unique, stable order)
    public Map<String, List<String>> getWordDiff(String oldText, String newText) {
        List<String> oldTokens = tokenize(oldText);
        List<String> newTokens = tokenize(newText);

        Map<String, Integer> oldCounts = counts(oldTokens);
        Map<String, Integer> newCounts = counts(newTokens);

        // use LinkedHashSet to preserve first-seen order while deduping
        Set<String> added = new LinkedHashSet<>();
        Set<String> removed = new LinkedHashSet<>();

        // Detect added words (present in new with greater count than old OR not present)
        for (String w : newCounts.keySet()) {
            int newC = newCounts.getOrDefault(w, 0);
            int oldC = oldCounts.getOrDefault(w, 0);
            if (newC > oldC) added.add(w);
        }

        // Detect removed words (present in old with greater count than new OR not present)
        for (String w : oldCounts.keySet()) {
            int oldC = oldCounts.getOrDefault(w, 0);
            int newC = newCounts.getOrDefault(w, 0);
            if (oldC > newC) removed.add(w);
        }

        Map<String, List<String>> result = new HashMap<>();
        result.put("added", new ArrayList<>(added));
        result.put("removed", new ArrayList<>(removed));
        return result;
    }

    private Map<String, Integer> counts(List<String> tokens) {
        Map<String, Integer> m = new LinkedHashMap<>();
        for (String t : tokens) m.put(t, m.getOrDefault(t, 0) + 1);
        return m;
    }
}

