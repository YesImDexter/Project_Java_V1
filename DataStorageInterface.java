// File: DataStorageInterface.java
// Creator: ABDUL HAFIY KAMALUDDIN BIN ABDUL RANI (101476)
// Tester: JEREMY TOMMY AJENG EMANG (99286)

import java.util.List;
import java.util.Map;

/**
 * Interface for user score data storage operations.
 */
public interface DataStorageInterface {
    /**
     * Saves a new score for a user (adds to their history).
     */
    void saveScore(String userId, int score) throws DataStorageException;

    /**
     * Gets all scores for a specific user.
     */
    List<Integer> getUserScores(String userId) throws DataStorageException;

    /**
     * Gets the latest score for a user.
     */
    int getLatestScore(String userId) throws DataStorageException;

    /**
     * Gets all scores from all users.
     */
    Map<String, List<Integer>> getAllScores() throws DataStorageException;

    /**
     * Gets the highest score for a user.
     */
    int getUserHighScore(String userId) throws DataStorageException;
}