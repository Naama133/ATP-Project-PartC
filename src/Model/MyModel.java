package Model;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.scene.control.Alert;

import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private Maze modelMaze;
    private int PlayerRow;
    private int PlayerCol;

    public MyModel() {
        this.modelMaze = null;
        this.PlayerRow = 0;
        this.PlayerCol = 0;
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
    public void solveMaze(Maze maze) { //todo
        //Solving Maze
        setChanged();
        notifyObservers("ModelSolvedMaze");
    }

    @Override
    public void getSolution() { //todo
        //return solution
    }

    public void generateMaze(int rows, int cols){ //create Maze
        IMazeGenerator generator = new MyMazeGenerator();
        Maze maze = null;
        try {
            maze = generator.generate(rows,cols);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.modelMaze  = maze;
        this.PlayerRow =maze.getStartPosition().getRowIndex();
        this.PlayerCol = maze.getStartPosition().getColumnIndex();
        System.out.println("maze");
        System.out.println(maze.getStartPosition().toString());
        System.out.println(maze.getGoalPosition().toString());
        setChanged();
        notifyObservers("ModelGenerateMaze");

    }

    public void UpdatePlayerPosition(int direction){ //Update Location of the character is the Maze
        String ActionMessage = "ModelUpdatePlayerPosition";
        switch (direction) {
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
        if(currPlayerPosition.equals(modelMaze.getGoalPosition().toString()))
            ActionMessage = "UserSolvedTheMaze";
        setChanged();
        notifyObservers(ActionMessage);
    }

    @Override
    public void assignObserver(Observer O) {
        this.addObserver(O);
    }

}
