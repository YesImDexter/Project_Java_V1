// File: DataStorageException.java
// Creator: Hafiy
// Tester: [Group member who will test this]

/**
 * Custom exception for data storage operations
 */
public class DataStorageException extends Exception {
    public DataStorageException(String message) {
        super(message);
    }
    
    public DataStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}