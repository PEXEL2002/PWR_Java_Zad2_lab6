package app;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageProcessor {

    private static int threadCount = 4;

    public static void setThreadCount(int count) {
        threadCount = Math.max(1, Math.min(4, count));
    }

    public static Image generateNegative(Image originalImage) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();

        WritableImage outputImage = new WritableImage(width, height);
        PixelReader reader = originalImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();

        Thread[] threads = new Thread[threadCount];
        int sliceHeight = height / threadCount;

        long start = System.currentTimeMillis();

        for (int t = 0; t < threadCount; t++) {
            final int startY = t * sliceHeight;
            final int endY = (t == threadCount - 1) ? height : startY + sliceHeight;

            threads[t] = new Thread(() -> {
                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < width; x++) {
                        Color color = reader.getColor(x, y);
                        Color negative = new Color(1.0 - color.getRed(), 1.0 - color.getGreen(), 1.0 - color.getBlue(), color.getOpacity());
                        writer.setColor(x, y, negative);
                    }
                }
            });
            threads[t].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                LoggerService.log("generateNegative przerwany: " + e.getMessage(), "ERROR");
            }
        }

        long end = System.currentTimeMillis();
        LoggerService.log("Negatyw wykonano", "INFO", end - start);
        return outputImage;
    }

    public static Image applyThreshold(Image originalImage, int threshold) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();

        WritableImage outputImage = new WritableImage(width, height);
        PixelReader reader = originalImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();

        Thread[] threads = new Thread[threadCount];
        int sliceHeight = height / threadCount;

        long start = System.currentTimeMillis();

        for (int t = 0; t < threadCount; t++) {
            final int startY = t * sliceHeight;
            final int endY = (t == threadCount - 1) ? height : startY + sliceHeight;

            threads[t] = new Thread(() -> {
                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < width; x++) {
                        Color color = reader.getColor(x, y);
                        double gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
                        Color result = (gray * 255) < threshold ? Color.BLACK : Color.WHITE;
                        writer.setColor(x, y, result);
                    }
                }
            });
            threads[t].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                LoggerService.log("applyThreshold przerwany: " + e.getMessage(), "ERROR");
            }
        }

        long end = System.currentTimeMillis();
        LoggerService.log("Progowanie wykonano", "INFO", end - start);
        return outputImage;
    }

    public static Image applyEdgeDetection(Image originalImage) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();

        WritableImage outputImage = new WritableImage(width, height);
        PixelReader reader = originalImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();

        Thread[] threads = new Thread[threadCount];
        int sliceHeight = height / threadCount;

        long start = System.currentTimeMillis();

        for (int t = 0; t < threadCount; t++) {
            final int startY = t * sliceHeight;
            final int endY = (t == threadCount - 1) ? height - 1 : Math.min(height - 1, startY + sliceHeight);

            threads[t] = new Thread(() -> {
                for (int y = Math.max(1, startY); y < endY; y++) {
                    for (int x = 1; x < width - 1; x++) {
                        double center = getGray(reader.getColor(x, y));
                        double right = getGray(reader.getColor(x + 1, y));
                        double bottom = getGray(reader.getColor(x, y + 1));

                        double edge = Math.abs(center - right) + Math.abs(center - bottom);
                        edge = Math.min(edge * 2, 1.0);

                        writer.setColor(x, y, new Color(edge, edge, edge, 1.0));
                    }
                }
            });
            threads[t].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                LoggerService.log("applyEdgeDetection przerwany: " + e.getMessage(), "ERROR");
            }
        }

        long end = System.currentTimeMillis();
        LoggerService.log("Konturowanie wykonano", "INFO", end - start);
        return outputImage;
    }

    private static double getGray(Color color) {
        return (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
    }
}
