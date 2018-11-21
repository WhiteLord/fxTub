package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(System.getProperty("user.dir"));
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("anotherOne.fxml"));
        primaryStage.setTitle("ScanX");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
        primaryStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
