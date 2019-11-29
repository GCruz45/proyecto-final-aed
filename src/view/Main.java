package view;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{

    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Contest Problem Solver");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}