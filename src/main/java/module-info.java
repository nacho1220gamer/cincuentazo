module cincuentazo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Export packages that exist
    exports cincuentazo.view;
    exports cincuentazo.controller;
    exports cincuentazo.model.card;
    exports cincuentazo.model.deck;
    exports cincuentazo.model.game;
    exports cincuentazo.model.player;
    exports cincuentazo.model.exceptions;

    // Open packages to javafx.fxml for reflection
    opens cincuentazo.controller to javafx.fxml;
    opens cincuentazo.view to javafx.fxml;
}