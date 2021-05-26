package View;

import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.beans.property.StringProperty;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

//make sure it extends Canvas of JAVA (not AWT)
public class MazeDisplayer extends Canvas {

    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();

    private Maze mazeDisplay;
    private int PlayerRow = 0;
    private int PlayerCol = 0;

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

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }


    public void drawMaze(Maze maze) {
        this.mazeDisplay = maze;
        draw();
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
            drawMazePlayer(graphicsContext, cellHeight, cellWidth);
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
}
