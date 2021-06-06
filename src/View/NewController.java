package View;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class NewController implements Initializable {

    public Label aboutText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String about = "\nHello, and welcome to our Maze Game!\n" +
                "\nIn this game you can generate different new mazes,\n" +
                "By the help of our three generators:\n " +
                "1. EmptyMazeGenerator (creates an empty maze)\n" +
                "2. SimpleMazeGenerator (creates a simply random maze)\n" +
                "3. MyMazeGenerator (creates maze by using the DFS algorithm)\n" +
                "\nIn addition, you can try to solve the maze by your self,\nAnd if you need a little help -\n" +
                "You can use one of our three solvers,\nwhich based on the following algorithms:\n" +
                "1. DepthFirstSearch (DFS)\n" +
                "2. BreadthFirstSearch (BFS)\n" +
                "3. BestFirstSearch (BEST)\n" +
                "\n" +
                "\n" +
                "Enjoy,\n" +
                "Dar Abu & Naama Baruch\n\n";

        aboutText.setText(about);
    }

}