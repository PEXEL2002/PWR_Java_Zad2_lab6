package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class HelloApplication extends Application {
    private Logger logger = Logger.getLogger(HelloApplication.class.getName());
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/app/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("Image Processor - Lab6");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop() {
        LoggerService.log("Zamknięcie aplikacji", "EXIT");
    }
    public static void main(String[] args) {
        launch();
    }
}
