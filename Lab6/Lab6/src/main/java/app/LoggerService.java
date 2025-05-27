package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerService {

    private static final String LOG_FILE = System.getProperty("user.home") + File.separator + "logs.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String message, String level, long timeMs) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String fullMessage = "[" + level.toUpperCase() + "] " + timestamp +
                " - " + message + " (czas: " + timeMs + " ms)";
        writeToFile(fullMessage);
    }

    public static void log(String message, String level) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String fullMessage = "[" + level.toUpperCase() + "] " + timestamp + " - " + message;
        writeToFile(fullMessage);
    }

    private static void writeToFile(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(message);
        } catch (IOException e) {
            System.err.println("Błąd zapisu logu: " + e.getMessage());
        }
    }
}
