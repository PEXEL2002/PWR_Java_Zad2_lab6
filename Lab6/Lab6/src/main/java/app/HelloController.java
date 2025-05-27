package app;

import static app.ImageProcessor.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class HelloController {
    private static final LoggerService logger = new LoggerService();
    @FXML public Button loadImage;
    @FXML public ComboBox<String> operationComboBox; // 🟢 parametryzacja typu
    @FXML public Button process;
    @FXML public Button saveButton;
    @FXML public Button rotateLeftButton;
    @FXML public Button rotateRightButton;
    @FXML private ImageView logoImageView;
    @FXML private ImageView originalImageView;
    @FXML private ImageView processedImageView;

    private Image originalImage;
    private Image processedImage;

    private long startTime;
    private long endTime;
    @FXML
    public void initialize() {
        try {
            Image logo = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/assets/PWR.jpeg")
            ));
            logoImageView.setImage(logo);
        } catch (Exception e) {
            System.out.println("Błąd ładowania loga: " + e.getMessage());
        }
        operationComboBox.getItems().addAll(
                "Negatyw",
                "Progowanie",
                "Konturowanie",
                "Skalowanie"
        );
        saveButton.setDisable(true);
        rotateLeftButton.setDisable(true);
        rotateRightButton.setDisable(true);
        logger.log("Uruchomienie Aplikacji", "START");
    }
    @FXML
    private void onLoadImage() {
        startTime = System.currentTimeMillis();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj obraz");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki JPG", "*.jpg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            originalImage = new Image(file.toURI().toString());
            processedImage = null;
            originalImageView.setImage(originalImage);
            processedImageView.setImage(processedImage);
            saveButton.setDisable(false);
            rotateLeftButton.setDisable(false);
            rotateRightButton.setDisable(false);
            endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            logger.log("Załadowano Obraz","INFO",  duration);
            showToast("Pomyślnie załadowano plik");
        }else{
            showToast("Nie udało się załadować pliku");
            logger.log("Nie udało się załadować pliku", "ERROR");
        }

    }
    @FXML
    private void onSaveImage() {
        if (originalImage == null) {
            showToast("Najpierw wczytaj obraz!");
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Zapis obrazu");
        dialog.setHeaderText("Podaj nazwę pliku (3–100 znaków)");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField nameField = new TextField();
        nameField.setPromptText("Nazwa pliku");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);
        Label noEditLabel = new Label("Na pliku nie zostały wykonane żadne operacje!");
        noEditLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        noEditLabel.setVisible(processedImage == null); // pokaż tylko jeśli brak operacji
        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 100) {
                nameField.setText(oldVal);
            }
            errorLabel.setVisible(false);
        });
        grid.add(new Label("Nazwa:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(errorLabel, 1, 1);
        grid.add(noEditLabel, 1, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name = nameField.getText().trim();
            if (name.length() < 3) {
                errorLabel.setText("Wpisz co najmniej 3 znaki");
                errorLabel.setVisible(true);
                onSaveImage(); // ponownie pokaż okno
                return;
            }
            Image toSave = processedImage != null ? processedImage : originalImage;
            File outputDir = new File(System.getProperty("user.home"), "Pictures");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            File file = new File(outputDir, name + ".jpg");
            if (file.exists()) {
                showToast("Plik " + name + ".jpg już istnieje w systemie. Podaj inną nazwę pliku!");
                return;
            }
            try {
                BufferedImage raw = SwingFXUtils.fromFXImage(toSave, null);
                BufferedImage rgb = new BufferedImage(raw.getWidth(), raw.getHeight(), BufferedImage.TYPE_INT_RGB);
                var g = rgb.createGraphics();
                g.drawImage(raw, 0, 0, java.awt.Color.WHITE, null);
                g.dispose();
                boolean success = ImageIO.write(rgb, "jpg", file);
                if (success) {
                    showToast("Zapisano obraz w pliku " + name + ".jpg");
                    logger.log("Zapisano Zdjęcie", "INFO");
                } else {
                    showToast("Nie udało się zapisać pliku " + name + ".jpg");
                    logger.log("Nie zapisano zdjęcia", "ERROR");
                }
            } catch (IOException e) {
                showToast("Nie udało się zapisać pliku " + name + ".jpg");
                logger.log("Nie zapisano zdjęcia", "ERROR");
            }
        } else {
            nameField.clear();
            errorLabel.setVisible(false);
        }
    }

    @FXML
    private void onApplyOperation() {
        if (originalImage == null) {
            showToast("Najpierw wczytaj obraz!");
            return;
        }
        String selectedOperation = (String) operationComboBox.getValue();
        if (selectedOperation == null || selectedOperation.isBlank()) {
            showToast("Nie wybrano operacji do wykonania");
            return;
        }
        switch (selectedOperation) {
            case "Negatyw" -> {
                startTime = System.currentTimeMillis();
                processedImage = ImageProcessor.generateNegative(originalImage);
                processedImageView.setImage(processedImage);
                endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                logger.log("Ukończono Negatyw","INFO", duration);
                showToast("Zastosowano: Negatyw");
            }
            case "Progowanie" -> {
                showThresholdDialog();
            }
            case "Konturowanie" -> {
                startTime = System.currentTimeMillis();
                processedImage = ImageProcessor.applyEdgeDetection(originalImage);
                processedImageView.setImage(processedImage);
                endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                logger.log("Ukończono Konturowanie","INFO", duration);
                showToast("Zastosowano: Konturowanie");
            }
            case "Skalowanie" -> {
                onScaleImage();
            }
            default -> {
                showToast("Nieznana operacja: " + selectedOperation);
            }
        }
    }

    @FXML
    private void rotateLeftImage() {
        startTime = System.currentTimeMillis();
        if (originalImage == null) {
            showToast("Najpierw wczytaj obraz!");
            return;
        }

        Image input = processedImage != null ? processedImage : originalImage;

        ImageView view = new ImageView(input);
        view.setRotate(-90); // obrót w lewo
        Image rotated = view.snapshot(null, null);

        processedImage = rotated;
        processedImageView.setImage(rotated);
        endTime = System.currentTimeMillis();
        showToast("Obrócono obraz w lewo");
        long duration = endTime - startTime;
        logger.log("Obrócono obraz w lewo","INFO", duration);
    }

    @FXML
    private void rotateRightImage() {
        startTime = System.currentTimeMillis();
        if (originalImage == null) {
            showToast("Najpierw wczytaj obraz!");
            return;
        }

        Image input = processedImage != null ? processedImage : originalImage;

        ImageView view = new ImageView(input);
        view.setRotate(90); // obrót w prawo
        Image rotated = view.snapshot(null, null);

        processedImage = rotated;
        processedImageView.setImage(rotated);
        endTime = System.currentTimeMillis();
        long  duration = endTime - startTime;
        showToast("Obrócono obraz w prawo");
        logger.log("Obrócono obraz w lewo","INFO", duration);
    }

    private void onScaleImage() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Skalowanie obrazu");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField widthField = new TextField();
        widthField.setPromptText("Szerokość (1–3000)");
        TextField heightField = new TextField();
        heightField.setPromptText("Wysokość (1–3000)");

        Label widthError = new Label("Pole jest wymagane");
        widthError.setStyle("-fx-text-fill: red;");
        widthError.setVisible(false);

        Label heightError = new Label("Pole jest wymagane");
        heightError.setStyle("-fx-text-fill: red;");
        heightError.setVisible(false);

        Button resetButton = new Button("Przywróć oryginalne wymiary");

        grid.add(new Label("Szerokość:"), 0, 0);
        grid.add(widthField, 1, 0);
        grid.add(widthError, 1, 1);
        grid.add(new Label("Wysokość:"), 0, 2);
        grid.add(heightField, 1, 2);
        grid.add(heightError, 1, 3);
        grid.add(resetButton, 0, 4, 2, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        resetButton.setOnAction(_ -> {
            processedImage = originalImage;
            processedImageView.setImage(originalImage);
            dialog.close();
        });
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean valid = true;
            String widthText = widthField.getText().trim();
            String heightText = heightField.getText().trim();
            widthError.setVisible(false);
            heightError.setVisible(false);
            int newWidth = -1;
            int newHeight = -1;
            if (widthText.isEmpty()) {
                widthError.setVisible(true);
                valid = false;
            } else {
                try {
                    newWidth = Integer.parseInt(widthText);
                    if (newWidth <= 0 || newWidth > 3000) {
                        widthError.setText("Wartość spoza zakresu");
                        widthError.setVisible(true);
                        valid = false;
                    }
                } catch (NumberFormatException e) {
                    widthError.setText("Niepoprawna liczba");
                    widthError.setVisible(true);
                    valid = false;
                }
            }
            if (heightText.isEmpty()) {
                heightError.setVisible(true);
                valid = false;
            } else {
                try {
                    newHeight = Integer.parseInt(heightText);
                    if (newHeight <= 0 || newHeight > 3000) {
                        heightError.setText("Wartość spoza zakresu");
                        heightError.setVisible(true);
                        valid = false;
                    }
                } catch (NumberFormatException e) {
                    heightError.setText("Niepoprawna liczba");
                    heightError.setVisible(true);
                    valid = false;
                }
            }
            if (!valid) {
                onScaleImage();
                return;
            }
            startTime = System.currentTimeMillis();
            Image input = processedImage != null ? processedImage : originalImage;
            ImageView view = new ImageView(input);
            view.setPreserveRatio(false);
            view.setFitWidth(newWidth);
            view.setFitHeight(newHeight);
            Image scaled = view.snapshot(null, null);
            processedImage = scaled;
            processedImageView.setImage(scaled);
            endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            logger.log("Obraz przeskalowany","INFO", duration);
            showToast("Obraz przeskalowany do " + newWidth + "×" + newHeight);
        }
    }
    private void showThresholdDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Progowanie");
        dialog.setHeaderText("Podaj wartość progu (0–255)");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField thresholdField = new TextField();
        thresholdField.setPromptText("Wartość progu");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);

        grid.add(new Label("Próg:"), 0, 0);
        grid.add(thresholdField, 1, 0);
        grid.add(errorLabel, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int threshold = Integer.parseInt(thresholdField.getText().trim());

                if (threshold < 0 || threshold > 255) {
                    showToast("Wartość progu musi być w zakresie 0–255");
                    return;
                }
                startTime = System.currentTimeMillis();
                processedImage = ImageProcessor.applyThreshold(originalImage, threshold);
                processedImageView.setImage(processedImage);
                endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                logger.log("Ukończono Progowanie","INFO", duration);
                showToast("Zastosowano progowanie z progiem " + threshold);

            } catch (NumberFormatException e) {
                showToast("Wprowadź poprawną wartość liczbową!");
            }
        }
    }
    private void showToast(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}