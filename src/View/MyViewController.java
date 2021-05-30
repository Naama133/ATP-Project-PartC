package View;

import Model.IModel;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
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
    private MyViewModel viewModel = MyViewModel.getInstance();
    private Maze viewMaze;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    private Solution ViewSolution;
    //StringProperty can listen to other StringProperty and change when the other change
    //we will bind them to the StringProperty of the labels when the scene initialize (by implement Initializable)
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    @Override
    //when scene uploads, active this function and bind labels
    //textProperty is StringProperty, we can bind those two textProperties
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }

/*    public void setViewModel(MyViewModel viewModel) {

        this.viewModel = viewModel;
    }*/

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

    //handle maze creation
    public void generateMaze(ActionEvent actionEvent) {
        if(mazeDisplayer.getDrawSolution())
            mazeDisplayer.ChangeDrawSolution();
        mazeDisplayer.deleteSolution();
        ViewSolution=null;
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
                    viewMaze = viewModel.getMaze(); //new maze have been created
                    mazeDisplayer.setMazeDisplay(viewMaze);
                    setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
                }
                case "ModelUpdatePlayerPosition" -> {
                    int rowViewModel = viewModel.getPlayerRow();
                    int colViewModel = viewModel.getPlayerCol();
                    setPlayerPosition(rowViewModel, colViewModel);}
                case "ModelSolvedMaze" -> {
                    ViewSolution = viewModel.getSolution();
                    mazeDisplayer.DrawWhenSolve(ViewSolution);
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
                    alert.setContentText("You won!! wowo!");
                    alert.show();
                    /*  //Gif pop up
                    Stage stage = new Stage();
                    stage.setTitle("YOU WON!!!");
                    VBox layout = new VBox();
                    HBox H = new HBox(5);
                    H.setAlignment(CENTER);
                    layout.setAlignment(CENTER);
                    javafx.scene.control.Button close = new javafx.scene.control.Button();
                    close.setText("Close");
                    H.getChildren().add(close);
                    layout.spacingProperty().setValue(10);
                    Image im = new Image("/Images/wonGif.gif");
                    ImageView image = new ImageView(im);
                    layout.getChildren().add(image);
                    layout.getChildren().add(H);
                    Scene scene = new Scene(layout, 520, 350);
                    scene.getStylesheets().add(getClass().getResource("/View/LoadScene.css").toExternalForm());
                    stage.setScene(scene);
                    stage.show();*/
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

    //todo - check all exceptions of last projects - handle them here
    //todo - not config - menu to choose generator, algorithm, number of threads
    //todo - press key before we have any maze  - exception
    //todo - move maze to the middle of the screen?
    //todo - add button to set size of game board
    public void mouseScrolled(ScrollEvent scrollEvent) {
        if(scrollEvent.isControlDown()) {
            double zoom = 1.05;
            double deltaY = scrollEvent.getDeltaY();
            if (deltaY < 0) {
                zoom = 2.0 - zoom;
            }

            double MaxHeight = mazeDisplayer.getParent().getBoundsInParent().getHeight();
            double MaxWidth =mazeDisplayer.getParent().getBoundsInParent().getWidth();

            double Dim = Math.min(MaxHeight,MaxWidth);

            if(mazeDisplayer.getHeight()*zoom<Dim && mazeDisplayer.getHeight()*zoom>1)
                mazeDisplayer.setHeight(mazeDisplayer.getHeight()*zoom);
            if(mazeDisplayer.getWidth()*zoom<Dim && mazeDisplayer.getHeight()*zoom>1)
                mazeDisplayer.setWidth(mazeDisplayer.getWidth()*zoom);
            mazeDisplayer.drawMaze();
        }
    }

    public void restartMaze(ActionEvent actionEvent) {
        viewModel.restartPlayer();
    }

}
