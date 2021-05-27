package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * Controller of the fxml (MyView) - responses for the representation of a model
 * implements IView interface
 */
public class MyViewController implements Initializable , Observer,IView {

    private MyViewModel viewModel;
    private Maze viewMaze;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;

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

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
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


    //handle maze creation
    public void generateMaze(ActionEvent actionEvent) {
        int rows = Integer.parseInt(textField_mazeRows.getText());
        int cols = Integer.parseInt(textField_mazeColumns.getText());
        viewModel.generateMaze(rows,cols);
    }

    public void setPlayerPosition(int row, int col) {
        mazeDisplayer.setPlayerPosition(row,col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    public void solveMaze(ActionEvent actionEvent) {
        viewModel.solveMaze(viewMaze);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
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
        //todo startPosition and endposition (in each maze creation) + player position after it moves

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
                    viewModel.getSolution();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Solving maze...");
                    alert.show();}
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
                }
            }
        }}

}
