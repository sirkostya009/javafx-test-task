package ua.sirkostya009.javafxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        var fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        var scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("UI");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
