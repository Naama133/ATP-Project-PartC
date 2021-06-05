package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Controller of the fxml (MyView) - responses for the representation of a model
 * implements IView interface
 */

public class MyViewController implements Initializable , Observer,IView {

    public Button generate_btn;
    public Button solution_btn;
    public Button restart_btn;
    public Pane pane;
    public Label playerRow;
    public Label playerCol;
    public Stage myStage;

    private MyViewModel viewModel = MyViewModel.getInstance();
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;

    //StringProperty can listen to other StringProperty and change when the other change
    //we will bind them to the StringProperty of the labels when the scene initialize (by implement Initializable)
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    //todo dar - drag - 20:00 in rotem recording + talked about log4j on start
    //todo dar - talked about window size changing (need to to add to the button + to the drag option) 26:30 ${pane.height}

    //todo: add music button (play & stop) & add volume scale

    Media gameSoundTrack = new Media(new File("./resources/music/JalebiBaby.mp3").toURI().toString());
    MediaPlayer GameMediaPlayer = new MediaPlayer(gameSoundTrack);
    Media winningSoundTrack = new Media(new File("./resources/music/WeAreTheChampions.mp3").toURI().toString());
    MediaPlayer WinMediaPlayer = new MediaPlayer(winningSoundTrack);

    @Override
    //when scene uploads, active this function and bind labels
    //textProperty is StringProperty, we can bind those two textProperties
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
/*        GameMediaPlayer.play(); //todo: remove the //
        GameMediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                GameMediaPlayer.seek(Duration.ZERO);
            }
        });*/ //todo: remove the //
        mazeDisplayer.widthProperty().bind(pane.widthProperty());
        mazeDisplayer.heightProperty().bind(pane.heightProperty());
    }

    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int row) {
        this.updatePlayerRow.set("" + row);
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int col) {
        this.updatePlayerCol.set("" + col);
    }

    public void createNewMaze(){
        if(mazeDisplayer.getDrawSolution())
            mazeDisplayer.ChangeDrawSolution();
        mazeDisplayer.deleteSolution();
        try {
            int rows = Integer.parseInt(textField_mazeRows.getText());
            int cols = Integer.parseInt(textField_mazeColumns.getText());
            viewModel.generateMaze(rows, cols);
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid input - Type only numbers, no spaces and signs.");
            alert.show();
        }
        solution_btn.setDisable(false);
        restart_btn.setDisable(false);
    }

    //handle maze creation
    public void generateMaze(ActionEvent actionEvent) {
      createNewMaze();
    }

    public void setPlayerPosition(int row, int col) {
        mazeDisplayer.setPlayerPosition(row,col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    public void solveMaze(ActionEvent actionEvent) {
        mazeDisplayer.ChangeDrawSolution();
        if(mazeDisplayer.getSolution()==null)
            viewModel.solveMaze();
        else
            mazeDisplayer.drawMaze();
    }

    //event handler - listen to key press event
    //keyEvent = argument of the event (we can use this to check with key is pressed)
    public void keyPressed(KeyEvent keyEvent) {
        viewModel.movePlayer(keyEvent);
        keyEvent.consume(); //if we have another handler - clean it
    }

    //focus on mouse click position
    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        String action = arg.toString();
        if(o instanceof MyViewModel) {
            switch (action) {//maze creation
                case "ModelGenerateMaze" -> {
                    mazeDisplayer.setMazeDisplay(viewModel.getMaze());
                    setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
                }
                case "ModelUpdatePlayerPosition" -> {
                    int rowViewModel = viewModel.getPlayerRow();
                    int colViewModel = viewModel.getPlayerCol();
                    setPlayerPosition(rowViewModel, colViewModel);}
                case "ModelSolvedMaze" -> {
                    mazeDisplayer.DrawWhenSolve(viewModel.getSolution());
                }
                case "BoundariesProblem" -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Invalid move - You went outside the boundaries of the game board");
                    alert.show();}
                case "Wall" -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Invalid move - You Stuck in the wall");
                    alert.show();}
                case "UserSolvedTheMaze" -> {
                    int rowViewModel = viewModel.getPlayerRow();
                    int colViewModel = viewModel.getPlayerCol();
                    setPlayerPosition(rowViewModel, colViewModel);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("You won!! wo wo!");
                    alert.show();
                    //GameMediaPlayer.stop(); //todo: remove the //
                    //WinMediaPlayer.play();
/*                    WinMediaPlayer.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            GameMediaPlayer.play();
                        }
                    });*/ //todo: remove the //
                }
                case "InvalidMazeSize" -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Invalid parameter row/column - must be bigger then 1 and max size 1000.");
                    alert.show();
                }
                case "restartPlayerPosition" -> {
                    if(mazeDisplayer.getDrawSolution())
                        mazeDisplayer.ChangeDrawSolution();
                    setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
                }
            }
        }}

    public void mouseScrolled(ScrollEvent scrollEvent) {
        //todo: correct the zoom (no it has constrains)
        //todo naama - update from email with rotem
        if(scrollEvent.isControlDown()) {
            double zoom = 1.02;
            double deltaY = scrollEvent.getDeltaY();
            if (deltaY < 0) {
                zoom = 0.98;
            }
            double newScaleX = mazeDisplayer.getScaleX() * zoom;
            double newScaleY = mazeDisplayer.getScaleY() * zoom;

            mazeDisplayer.setScaleX(newScaleX);
            mazeDisplayer.setScaleY(newScaleY);
            mazeDisplayer.drawMaze();
        }
    }

    public void restartMaze(ActionEvent actionEvent) {
        viewModel.restartPlayer();
    }

    public void helperFunctionOpenStage(String title, int sceneSizeW, int sceneSizeH, boolean ExitWindow, boolean PropertiesWindow){
        Stage stage = new Stage();
        stage.setTitle(title);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(title + ".fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("FXML loading problem");
            alert.show();
        }
        Scene scene = new Scene(root, sceneSizeW, sceneSizeH);
        scene.getStylesheets().add(getClass().getResource(title+".css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL); //lock the window
        if(ExitWindow){
            stage.setTitle("");

            ExitController exitController = fxmlLoader.getController();
            Button cnl_btn = exitController.cancel_btn;

            cnl_btn.setOnAction(e->{
                stage.close();
                myStage.show();
            });
        }
        if(PropertiesWindow){
            PropertiesController propertiesController = fxmlLoader.getController();
            Button ok_btn = propertiesController.OK_btn;

            ok_btn.setOnAction(e->{
                if(mazeDisplayer.getDrawSolution())
                    mazeDisplayer.ChangeDrawSolution();

                mazeDisplayer.deleteSolution();
                viewModel.deleteSolution();
                propertiesController.changeConfiguration(e);
                stage.close();
                mazeDisplayer.drawMaze();
            });
        }
        stage.show();
    }

    public void aboutWindow(ActionEvent actionEvent) {
        helperFunctionOpenStage("About", 450,420, false, false);
    }

    public void helpWindow(ActionEvent actionEvent) {

        helperFunctionOpenStage("Help", 750, 550, false, false);
    }

    public void propertiesWindow(ActionEvent actionEvent) {
        helperFunctionOpenStage("Properties", 400, 280, false, true);
    }

    public void exitGame(ActionEvent actionEvent) {//todo: need to check about the window -the primary Stage despairs
        checkExitWanted();
    }

    public void checkExitWanted(){
        helperFunctionOpenStage("Exit", 325, 150, true, false);
    }
    public void loadGame(ActionEvent actionEvent) {
        //todo
        System.out.println("load");
    }

    public void saveGame(ActionEvent actionEvent) {
        //todo
        System.out.println("save");

    }

    public void newGame(ActionEvent actionEvent) {
        //todo
        System.out.println("new");

    }

    public void setStageAndScene(Stage primaryStage) {
        myStage = primaryStage;
    }
}
