// File: DataStorage.java
// Creator: ABDUL HAFIY KAMALUDDIN BIN ABDUL RANI (101476)
// Tester: JEREMY TOMMY AJENG EMANG (99286)

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles persistent storage of user quiz scores.
 */
public class DataStorage implements DataStorageInterface {
    private static final String SCORES_FILE = "user_scores.txt";

    /**
     * Save a new score for a user.
     */
    @Override
    public void saveScore(String userId, int score) throws DataStorageException {
        validateInput(userId, score);

        try {
            // Read existing data
            Map<String, List<Integer>> allScores = loadAllScoresFromFile();

            // Add new score
            allScores.computeIfAbsent(userId, k -> new ArrayList<>()).add(score);

            // Save back to file
            saveAllScoresToFile(allScores);
        } catch (IOException e) {
            throw new DataStorageException("Failed to save score", e);
        }
    }

    /**
     * Get all scores for a specific user.
     */
    @Override
    public List<Integer> getUserScores(String userId) throws DataStorageException {
        try {
            Map<String, List<Integer>> allScores = loadAllScoresFromFile();
            return allScores.getOrDefault(userId, Collections.emptyList());
        } catch (IOException e) {
            throw new DataStorageException("Failed to load user scores", e);
        }
    }

    /**
     * Get the latest score for a user.
     */
    @Override
    public int getLatestScore(String userId) throws DataStorageException {
        List<Integer> scores = getUserScores(userId);
        if (scores.isEmpty()) {
            throw new DataStorageException("No scores found for user: " + userId);
        }
        return scores.get(scores.size() - 1);
    }

    /**
     * Get all scores from all users.
     */
    @Override
    public Map<String, List<Integer>> getAllScores() throws DataStorageException {
        try {
            return loadAllScoresFromFile();
        } catch (IOException e) {
            throw new DataStorageException("Failed to load all scores", e);
        }
    }

    /**
     * Get the highest score for a user.
     */
    @Override
    public int getUserHighScore(String userId) throws DataStorageException {
        List<Integer> scores = getUserScores(userId);
        if (scores.isEmpty()) {
            throw new DataStorageException("No scores found for user: " + userId);
        }
        return Collections.max(scores);
    }

    // --- Helper methods ---

    /**
     * Loads all scores from the file into a map.
     */
    private Map<String, List<Integer>> loadAllScoresFromFile() throws IOException {
        Map<String, List<Integer>> scoresMap = new HashMap<>();

        File file = new File(SCORES_FILE);
        if (!file.exists()) {
            return scoresMap;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    String userId = parts[0];
                    List<Integer> scores = Arrays.stream(parts[1].split(","))
                        .filter(s -> !s.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                    scoresMap.put(userId, scores);
                }
            }
        }

        return scoresMap;
    }

    /**
     * Saves all scores from the map back to the file.
     */
    private void saveAllScoresToFile(Map<String, List<Integer>> scoresMap) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCORES_FILE))) {
            for (Map.Entry<String, List<Integer>> entry : scoresMap.entrySet()) {
                String scores = entry.getValue().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
                writer.println(entry.getKey() + ":" + scores);
            }
        }
    }

    /**
     * Validates userId and score input.
     */
    private void validateInput(String userId, int score) throws DataStorageException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new DataStorageException("User ID cannot be null or empty");
        }
        if (score < 0 || score > 100) {
            throw new DataStorageException("Score must be between 0 and 100");
        }
    }
}