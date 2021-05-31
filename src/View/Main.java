package View;

import Model.*;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Maze Game");
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        IModel model = MyModel.getInstance();
        MyViewModel viewModel = MyViewModel.getInstance();
        MyViewController view =fxmlLoader.getController();
        viewModel.addObserver(view);
        primaryStage.setOnCloseRequest(e->{  //todo : clean closer (X or exit button)
            closeProgram(model);
        });
        primaryStage.show();
    }

    public void closeProgram(IModel model) {
        model.shutDownServers();
    }

    public static void main(String[] args) {
        launch(args);
    }
}