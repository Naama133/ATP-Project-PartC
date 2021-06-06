package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.beans.property.StringProperty;
import javafx.scene.transform.Scale;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

//make sure it extends Canvas of JAVA (not AWT)
public class MazeDisplayer extends Canvas {
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameStartPosition = new SimpleStringProperty();
    StringProperty imageFileNameGoalPosition = new SimpleStringProperty();
    StringProperty imageFileNameSolution = new SimpleStringProperty();

    private Maze mazeDisplay;
    private int PlayerRow = 0;
    private int PlayerCol = 0;
    private Boolean drawSolution = false;
    private Solution solution = null;

    public MazeDisplayer() {
        widthProperty().addListener(e -> draw());
        heightProperty().addListener(e -> draw());
    }

    public void setMazeDisplay(Maze mazeDisplay) {
        this.mazeDisplay = mazeDisplay;
    }

    public void ChangeDrawSolution() {
        if(drawSolution)
            drawSolution=false;
        else
            drawSolution=true;
    }

    public void deleteSolution() {
        this.solution = null;
    }

    public Solution getSolution() {
        return solution;
    }

    public Boolean getDrawSolution() {
        return drawSolution;
    }

    public void setImageFileNameStartPosition(String imageFileNameStartPosition) {
        this.imageFileNameStartPosition.set(imageFileNameStartPosition);
    }

    public void setImageFileNameGoalPosition(String imageFileNameGoalPosition) {
        this.imageFileNameGoalPosition.set(imageFileNameGoalPosition);
    }

    public int getPlayerRow() {
        return PlayerRow;
    }

    public int getPlayerCol() {
        return PlayerCol;
    }

    public void setPlayerPosition(int Row, int Col) {
        PlayerRow = Row;
        PlayerCol = Col;
        draw(); //change the maze (draw again) because player moved
    }

    public void DrawWhenSolve(Solution ViewSolution){
        solution=ViewSolution;
        draw();
    }

    //intellij know the StringProperty have a string, so it returns the String it contains
    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    //we can change the image while running
    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String getImageFileNameStartPosition() {
        return imageFileNameStartPosition.get();
    }

    public String getImageFileNameGoalPosition() {
        return imageFileNameGoalPosition.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }
    public String getImageFileNameSolution() {
        return imageFileNameSolution.get();
    }

    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.imageFileNameSolution.set(imageFileNameSolution);
    }

    private void draw() {
        if(mazeDisplay != null){
            //find the canvas sizes, and split into rows*cols cells
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = mazeDisplay.getRows();
            int cols = mazeDisplay.getColumns();

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            //define where to draw over the canvas
            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas: (if we already draw over the canvas)
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight); //clearRect is the attribute that draw over the canvas

            drawMazeWalls(graphicsContext, rows, cols, cellHeight, cellWidth);

            if(drawSolution)
                if(solution==null) { //todo: check if needed (need to catch this no-solution exception before)
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("there is no solution to draw");
                    alert.show();
                }
                else
                    drawSolution(graphicsContext, cellHeight, cellWidth);
            drawMazeStartAndGoal(graphicsContext, cellHeight, cellWidth);
            drawMazePlayer(graphicsContext, cellHeight, cellWidth);


        }
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        Image solutionImage = null;
        try {
            solutionImage = new Image(new FileInputStream(getImageFileNameSolution()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no image for the solution");
        }

        graphicsContext.setFill(Color.RED); //the color that we want to add in our draw
        ArrayList<AState> solutionPath = solution.getSolutionPath();

        for (int i = 0; i < solutionPath.size(); i++) {
            String position = solutionPath.get(i).toString();
            int index = position.indexOf(",");
            String posX = position.substring(1,index);
            String posY = position.substring(index+1, position.length()-1);

            double x = Integer.parseInt(posX) * cellHeight;
            double y = Integer.parseInt(posY) * cellWidth;
            if(solutionImage== null)
                graphicsContext.fillRect(y, x, cellWidth, cellHeight);
            else
                graphicsContext.drawImage(solutionImage, y, x, cellWidth, cellHeight);
        }
    }

    private void drawMazePlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        Image PlayerImage = null;
        try {
            PlayerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no Player image");
        }
        double x = getPlayerCol() * cellWidth; //change by each cell size
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);
        if(PlayerImage == null){
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        }
        else{
            graphicsContext.drawImage(PlayerImage, x, y, cellWidth, cellHeight);

        }
    }

    private void drawMazeStartAndGoal(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        Image StartImage = null;
        Image GoalImage = null;
        try {
            StartImage = new Image(new FileInputStream(getImageFileNameStartPosition()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no Start image");
        }
        try {
            GoalImage = new Image(new FileInputStream(getImageFileNameGoalPosition()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no Goal image");
        }
        double x_Start = mazeDisplay.getStartPosition().getColumnIndex() * cellWidth;
        double y_Start = mazeDisplay.getStartPosition().getRowIndex() * cellHeight;
        double x_Goal = mazeDisplay.getGoalPosition().getColumnIndex() * cellWidth;
        double y_Goal = mazeDisplay.getGoalPosition().getRowIndex() * cellHeight;
        graphicsContext.setFill(Color.BLUEVIOLET); //the color that we want to add in our draw
        if(StartImage== null)
            graphicsContext.fillRect(x_Start, y_Start, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(StartImage, x_Start, y_Start, cellWidth, cellHeight);
        graphicsContext.setFill(Color.GREENYELLOW); //the color that we want to add in our draw
        if(GoalImage== null)
            graphicsContext.fillRect(x_Goal, y_Goal, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(GoalImage, x_Goal, y_Goal, cellWidth, cellHeight);
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, int rows, int cols, double cellHeight, double cellWidth) {
        Image wallImage = null;
        try {
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image");
        }

        graphicsContext.setFill(Color.RED); //the color that we want to add in our draw

        //iterate over all cells, if the cell value = 1, fill the cell with red color
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(mazeDisplay.getMazeContent()[i][j] == 1){
                    //if it is a wall:
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if(wallImage== null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    public void drawMaze(){
        draw();
    }

}
