package View;

import Model.*;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    //todo - check all exceptions of last projects - handle them here
    //todo - press key before we have any maze  - correct the error
    //todo - add comments to all files
    //todo - no printStackTrace - all exceptions will be shown in alert window!
    //todo : if stuck in wall because of mouse drag - show one error
    //todo: music correction

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Maze Game");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        IModel model = MyModel.getInstance();
        MyViewModel viewModel = MyViewModel.getInstance();
        MyViewController view =fxmlLoader.getController();
        viewModel.addObserver(view);
        MyViewController MvController = fxmlLoader.getController();
        MvController.setStageAndScene(primaryStage);
        primaryStage.setOnCloseRequest(e->{
            view.checkExitWanted();
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