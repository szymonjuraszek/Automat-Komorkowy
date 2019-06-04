package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.Stage;

public class Main extends Application {

    Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        Parent root1 = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Parent root2 = FXMLLoader.load(getClass().getResource("gameInLife.fxml"));

        Scene scene1 = new Scene(root1);
        Scene scene2 = new Scene(root2);

        MenuBar menuBar= (MenuBar) root1.getChildrenUnmodifiable().get(8);
        MenuBar menuBar2= (MenuBar) root2.getChildrenUnmodifiable().get(0);

        RadioMenuItem radioMenuItem1 = (RadioMenuItem) menuBar.getMenus().get(0).getItems().get(0);
        RadioMenuItem radioMenuItem2 = (RadioMenuItem) menuBar2.getMenus().get(0).getItems().get(0);

        radioMenuItem2.addEventHandler(ActionEvent.ACTION,(event) -> {
            window.setScene(scene1);

        });
        radioMenuItem1.addEventHandler(ActionEvent.ACTION,(event) ->{
            window.setScene(scene2);
        });

        primaryStage.setResizable(false);
        primaryStage.setTitle("Automat Komorkowy");
        primaryStage.setScene(scene1);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
