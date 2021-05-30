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
        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view =fxmlLoader.getController();
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        //todo : add clean closer
        primaryStage.show();
        //playAudio("resources/music/song.mp3"); //todo dar
        //todo dar: add restart maze button

    }

/*    protected void playAudio(String audio) { //todo dar
        String soundTrack = audio;
        Media music = new Media(new File(soundTrack).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(music);
        mediaPlayer.play();
    }*/

    public static void main(String[] args) {
        launch(args);
    }
}


