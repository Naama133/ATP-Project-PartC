package View;

import Model.*;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Maze Game");
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view =fxmlLoader.getController();
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        //todo : add clean closer
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
