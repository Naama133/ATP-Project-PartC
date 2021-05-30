package Model;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.*;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private Maze modelMaze;
    private int PlayerRow;
    private int PlayerCol;
    private Solution modelMazeSolution;
    private SearchableMaze searchableMaze;
    private ISearchingAlgorithm searcher;

    public MyModel() {
        modelMaze = null;
        PlayerRow = 0;
        PlayerCol = 0;
        modelMazeSolution = null;
        searchableMaze = null;
    }

    public Maze getMaze(){
        return modelMaze;
    }

    public int getPlayerRow() {
        return PlayerRow;
    }

    public int getPlayerCol() {
        return PlayerCol;
    }

    @Override
    public void solveMaze() {
        //Solving Maze
        try {
            searchableMaze = new SearchableMaze(modelMaze);
        } catch (Exception e) {
            e.printStackTrace();
        }
        searcher = new BestFirstSearch(); //todo : get from user
        try {
            modelMazeSolution = searcher.solve(searchableMaze);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setChanged();
        notifyObservers("ModelSolvedMaze");
    }

    @Override
    public Solution getSolution() {
        return modelMazeSolution;
    }

    public void generateMaze(int rows, int cols){ //create Maze
       String action = "ModelGenerateMaze";
        if (rows < 2 || cols < 2 || rows >1000 || cols > 1000)
            action = "InvalidMazeSize";
        else{
        IMazeGenerator generator = new MyMazeGenerator();  //todo : get from user
        Maze maze = null;
        try {
            maze = generator.generate(rows,cols);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.modelMaze  = maze;
        this.PlayerRow =maze.getStartPosition().getRowIndex();
        this.PlayerCol = maze.getStartPosition().getColumnIndex();}

        setChanged();
        notifyObservers(action);

    }

    public void UpdatePlayerPosition(int direction){ //Update Location of the character is the Maze
        String ActionMessage = "ModelUpdatePlayerPosition";
        switch (direction) {
            case 0: //todo dar
                PlayerRow =modelMaze.getStartPosition().getRowIndex();
                PlayerCol = modelMaze.getStartPosition().getColumnIndex();
                ActionMessage = "restartPlayerPosition";
                break;
            case 8:
                if(PlayerRow>0)
                    if(modelMaze.getMazeContent()[PlayerRow-1][PlayerCol]==0)
                        PlayerRow--; //UP
                    else{ActionMessage = "Wall";}
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 2:
                if(PlayerRow<modelMaze.getRows()-1)
                    if(modelMaze.getMazeContent()[PlayerRow+1][PlayerCol]==0)
                            PlayerRow++; //DOWN
                    else{ActionMessage = "Wall";}
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 4:
                if(PlayerCol>0)
                    if(modelMaze.getMazeContent()[PlayerRow][PlayerCol-1]==0)
                        PlayerCol--; //LEFT
                    else{ActionMessage = "Wall";}
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 6:
                if(PlayerCol<modelMaze.getColumns()-1)
                    if(modelMaze.getMazeContent()[PlayerRow][PlayerCol+1]==0)
                        PlayerCol++; //RIGHT
                    else{ActionMessage = "Wall";}
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 1:
                if((PlayerRow<modelMaze.getRows()-1)&&(PlayerCol>0)){
                    if(modelMaze.getMazeContent()[PlayerRow+1][PlayerCol-1]==0){
                        PlayerRow++; //DOWN
                        PlayerCol--; //LEFT
                    }
                    else{ActionMessage = "Wall";}
                }
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 3:
                if((PlayerRow<modelMaze.getRows()-1)&&(PlayerCol<modelMaze.getColumns()-1)){
                    if(modelMaze.getMazeContent()[PlayerRow+1][PlayerCol+1]==0){
                        PlayerRow++; //DOWN
                        PlayerCol++; //RIGHT
                    }
                    else{ActionMessage = "Wall";}
                }
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 7:
                if((PlayerRow>0)&&(PlayerCol>0)){
                    if(modelMaze.getMazeContent()[PlayerRow-1][PlayerCol-1]==0){
                        PlayerRow--; //UP
                        PlayerCol--; //LEFT
                    }
                    else{ActionMessage = "Wall";}
                }
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 9:
                if((PlayerRow>0)&&(PlayerCol<modelMaze.getColumns()-1)){
                    if(modelMaze.getMazeContent()[PlayerRow-1][PlayerCol+1]==0){
                        PlayerRow--; //UP
                        PlayerCol++; //RIGHT
                    }
                    else{ActionMessage = "Wall";}
                }
                else{ActionMessage = "BoundariesProblem";}
                break;
            default:
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Invalid Key, press only 1,2,3,4,6,7,8,9 to move");
                alert.show();
        }
        String currPlayerPosition = "{" + PlayerRow + "," + PlayerCol + "}";
        if(currPlayerPosition.equals(modelMaze.getGoalPosition().toString())) //todo: solve message again and again
            ActionMessage = "UserSolvedTheMaze";
        setChanged();
        notifyObservers(ActionMessage);
    }

    //todo : checkBox to choose the theme of the game (and music?)

    @Override
    public void assignObserver(Observer O) {
        this.addObserver(O);
    }

}
