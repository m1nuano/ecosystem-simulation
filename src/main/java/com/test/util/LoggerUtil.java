package com.test.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {
    private static final StringBuilder logBuffer = new StringBuilder();
    private static Path logFilePath; // Path to the current ecosystem log file

    // Initialize the log file in the ecosystem folder
    public static void initializeLogFile(String ecosystemName) {
        logFilePath = Paths.get("ecosystems", ecosystemName, ecosystemName+"_log.txt");
        try {
            // Make sure the directory exists and create or clear the log file
            Files.createDirectories(logFilePath.getParent());
            Files.newBufferedWriter(logFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).close();
            logInfo("Log initialized for ecosystem: " + ecosystemName);
        } catch (IOException e) {
            System.err.println("Failed to initialize log file: " + e.getMessage());
        }
    }

    public static void logInfo(String message) {
        log("INFO", message);
    }

    public static void logError(String message, Exception e) {
        log("ERROR", message + ": " + e.getMessage());
    }

    // General method for writing logs indicating the level
    private static void log(String logLevel, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedMessage = String.format("[%s] [%s]: %s", timestamp, logLevel, message);
        logBuffer.append(formattedMessage).append(System.lineSeparator());

        if (logFilePath != null) {
            try (BufferedWriter writer = Files.newBufferedWriter(logFilePath, StandardOpenOption.APPEND)) {
                writer.write(formattedMessage + System.lineSeparator());
            } catch (IOException e) {
                System.err.println("Error writing to log file: " + e.getMessage());
            }
        } else {
            System.err.println("Log file path is not initialized. Call initializeLogFile() first.");
        }
    }

    // Transfer the log buffer to the target ecosystem file
    public static void flushLogToFile(Path targetLogPath, boolean append) {
        try {
            Files.createDirectories(targetLogPath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(
                    targetLogPath, append ? StandardOpenOption.APPEND : StandardOpenOption.CREATE)) {
                writer.write(logBuffer.toString());
                logBuffer.setLength(0); // Clear the buffer after writing
                System.out.println("Log successfully written to " + targetLogPath);
            }
        } catch (IOException e) {
            System.err.println("Error flushing log to file: " + e.getMessage());
        }
    }
}
