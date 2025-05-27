module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires java.logging;
    opens app to javafx.fxml;
    exports app;
}