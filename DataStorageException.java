// File: DataStorageException.java
// Creator: ABDUL HAFIY KAMALUDDIN BIN ABDUL RANI (101476)
// Tester: JEREMY TOMMY AJENG EMANG (99286)

/**
 * Custom exception for data storage operations.
 */
public class DataStorageException extends Exception {
    public DataStorageException(String message) {
        super(message);
    }

    public DataStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}