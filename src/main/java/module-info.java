module com.example.miniproyecto3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.desktop;

    opens cincuentazo to javafx.fxml;
    opens cincuentazo.controller to javafx.fxml;
    opens cincuentazo.view to javafx.fxml;

    exports cincuentazo;
    exports cincuentazo.controller;
    exports cincuentazo.view;
}
