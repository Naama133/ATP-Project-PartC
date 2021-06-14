package View;import ViewModel.MyViewModel;import algorithms.mazeGenerators.Maze;import javafx.beans.property.SimpleStringProperty;import javafx.beans.property.StringProperty;import javafx.event.ActionEvent;import javafx.fxml.FXMLLoader;import javafx.fxml.Initializable;import javafx.scene.Parent;import javafx.scene.Scene;import javafx.scene.control.*;import javafx.scene.image.Image;import javafx.scene.image.ImageView;import javafx.scene.input.*;import java.io.*;import java.net.URL;import java.util.Observable;import java.util.Observer;import java.util.ResourceBundle;import javafx.scene.layout.Pane;import javafx.scene.media.Media;import javafx.scene.media.MediaPlayer;import javafx.scene.text.Text;import javafx.scene.transform.Scale;import javafx.stage.FileChooser;import javafx.stage.Modality;import javafx.stage.Stage;import javafx.util.Duration;import javax.swing.filechooser.FileSystemView;/** * Controller of the fxml (MyView) - responses for the representation of a model * implements IView interface */public class MyViewController implements Initializable , Observer,IView {    public ChoiceBox choosePlayer; //user can choose the player    public Button generate_btn; //buttons of the game (generate maze, solve it, restart game)    public Button solution_btn;    public Button restart_btn;    public Pane pane; //pane that holds the maze canvas    public Label playerRow; //player position display    public Label playerCol;    public Stage myStage; //main stage    public Button musicOnOff; //music play/off button    public Text currentSong;    public Text pearlsNumber; //display the number of pearls that the user collected    private double mouseX ; //mouse position in the board    private double mouseY;    private MyViewModel viewModel = MyViewModel.getInstance(); //viewModel instance (single tone)    public TextField textField_mazeRows; //maze dimensions text fields    public TextField textField_mazeColumns;    public MazeDisplayer mazeDisplayer;    StringProperty updatePlayerRow = new SimpleStringProperty(); //listen to other StringProperty and change together by binding    StringProperty updatePlayerCol = new SimpleStringProperty();    //media players (user can choose the game sound - 3 option to choose from)    MediaPlayer mp; //will hold the current media player    MediaPlayer OldMp; //will hold the previous media player    Media gameSoundTrack = new Media(new File("resources/music/UnderTheSea.mp3").toURI().toString());    MediaPlayer GameMediaPlayer = new MediaPlayer(gameSoundTrack);    Media gameSoundTrack2 = new Media(new File("resources/music/KissTheGirl.mp3").toURI().toString());    MediaPlayer GameMediaPlayer2 = new MediaPlayer(gameSoundTrack2);    Media gameSoundTrack3 = new Media(new File("resources/music/PartOfYourWorld.mp3").toURI().toString());    MediaPlayer GameMediaPlayer3 = new MediaPlayer(gameSoundTrack3);    Media winningSoundTrack = new Media(new File("resources/music/WeAreTheChampions.mp3").toURI().toString());    MediaPlayer WinMediaPlayer = new MediaPlayer(winningSoundTrack);    Boolean pausePressed = false; //marked if pause pressed    @Override    //when scene uploads, active this function and bind labels    public void initialize(URL url, ResourceBundle resourceBundle) {        mp = GameMediaPlayer;        OldMp = GameMediaPlayer;        PlayerGameSoundTrack(0); //play gameSoundTrack        choosePlayer.getItems().addAll("Ariel", "Ursula", "Flounder", "Sebastian"); //options for players        choosePlayer.setValue("Ariel"); //default value for player        mazeDisplayer.setPlayer(getPlayer()); //update the player the mazeDisplayer holds        playerRow.textProperty().bind(updatePlayerRow); //show player current position (indexes)        playerCol.textProperty().bind(updatePlayerCol);        mazeDisplayer.widthProperty().bind(pane.widthProperty()); //bind pane and mazeDisplayer sizes (for resize event)        mazeDisplayer.heightProperty().bind(pane.heightProperty());        pearlsNumber.setText("0");        mazeDisplayer.setPearlsNumberView(pearlsNumber); //mazeDisplayer will update the pearlsNumber text view when the player collects them    }    public void PlayerGameSoundTrack(int StartTime){        if(pausePressed)            mp.setVolume(0);        else            mp.setVolume(1);        mp.play(); //play MediaPlayer content        mp.setStartTime(Duration.seconds(StartTime));        mp.setOnEndOfMedia(new Runnable() { //when the music over - start again            @Override            public void run() {                mp.seek(Duration.ZERO);}});    }    //setters & getters of player fields    public String getUpdatePlayerRow() {return updatePlayerRow.get();}    public void setUpdatePlayerRow(int row) {this.updatePlayerRow.set("" + row);}    public String getUpdatePlayerCol() {return updatePlayerCol.get();}    public void setUpdatePlayerCol(int col) { this.updatePlayerCol.set("" + col);}    public void setPlayerPosition(int row, int col) {        mazeDisplayer.setPlayerPosition(row,col); //update mazeDisplayer player position (draw again)        setUpdatePlayerRow(row);        setUpdatePlayerCol(col);}    //handle maze creation    public void generateMaze(ActionEvent actionEvent) {        try {            int rows = Integer.parseInt(textField_mazeRows.getText()); //take user input for rows & columns            int cols = Integer.parseInt(textField_mazeColumns.getText());            if (rows < 2 || cols < 2 || rows >1000 || cols > 1000) { //check that it's valid                ErrorMessage("Invalid parameter row/column -\nmust be bigger then 1 and max size 1000.");                textField_mazeRows.setText(String.valueOf(viewModel.getMaze().getRows()));                textField_mazeColumns.setText(String.valueOf(String.valueOf(viewModel.getMaze().getColumns())));            }            else                viewModel.generateMaze(rows, cols);} //generate maze, by ask viewModel to handle this event        catch (Exception e){            ErrorMessage("Invalid input - Type only numbers, no spaces and signs.");            textField_mazeRows.setText(String.valueOf(viewModel.getMaze().getRows()));            textField_mazeColumns.setText(String.valueOf(String.valueOf(viewModel.getMaze().getColumns())));        }        actionEvent.consume();}    //handle solve maze request    public void solveMaze(ActionEvent actionEvent) {        mazeDisplayer.ChangeDrawSolution(solution_btn); //ask mazeDisplayer to change DrawSolution field        if(mazeDisplayer.getSolution()==null) //if we don't have the current maze solutin (first time that we ask for solution)            viewModel.solveMaze(); //ask viewModel to handle this event        else            mazeDisplayer.drawMaze(); //if we already have a solution for this maze - draw it        actionEvent.consume();}    //event handler - listen to key press event    public void keyPressed(KeyEvent keyEvent) {        viewModel.movePlayer(keyEvent); //move player (if it's a valid move)        keyEvent.consume();    }    //focus on mouse click position    public void mouseClicked(MouseEvent mouseEvent) {        mazeDisplayer.requestFocus();        mouseEvent.consume();    }    @Override    public void update(Observable o, Object arg) { //handle update messages        String action = arg.toString();        if(o instanceof MyViewModel) {            switch (action) {//maze creation                case "ModelGenerateMaze","ModelLoadedMaze" -> {                    mazeDisplayer.FalseDrawSolution(solution_btn); //don't draw the solution                    mazeDisplayer.deleteSolution(); //delete the old solution                    mazeDisplayer.setMazeDisplay(viewModel.getMaze()); //set a new maze                    setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol()); //update player position, and draw again                    solution_btn.setDisable(false); //off the disable of the solution & restart buttons                    restart_btn.setDisable(false);}                case "ModelUpdatePlayerPosition" -> {setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());} //set new position for the player                case "ModelSolvedMaze" -> {mazeDisplayer.DrawWhenSolve(viewModel.getSolution());} //draw the solution                case "BoundariesProblem", "Wall" -> {//do nothing here                }                case "UserSolvedTheMaze" -> {                    setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());                    mp.stop(); //play the wonGame music                    OldMp = mp;                    mp = WinMediaPlayer;                    PlayerGameSoundTrack(0); //play WinMediaPlayer                    currentSong.setText("We Are The Champions");                    helperFunctionOpenStage("Win", 375, 340, false, false, true); //open winning stage                }                case "restartPlayerPosition" -> {                    mazeDisplayer.FalseDrawSolution(solution_btn); //hide solution & restart player position in the maze                    setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());                }            }        }}    //handle mouse scrolling event    public void mouseScrolled(ScrollEvent scrollEvent) {        double zoom =  0.02;        if(scrollEvent.isControlDown()) { //if control key is pressed -> it's a zoom action            double deltaY = scrollEvent.getDeltaY(); //find the wheel scrolling delta            if (deltaY < 0) {                zoom = -0.02;            }            Scale newScale = new Scale(); //change the scale of the pane            newScale.setX(pane.getScaleX() + zoom);            newScale.setY(pane.getScaleY() + zoom);            newScale.setPivotX(pane.getScaleX());            newScale.setPivotY(pane.getScaleY());            pane.getTransforms().add(newScale);            scrollEvent.consume();        }    }    //restart the game    public void restartMaze(ActionEvent actionEvent) {        mazeDisplayer.restartSolutionPath();        viewModel.restartPlayer();        actionEvent.consume();    }    //this function will open a new stage, by the given arguments (size, title)    public void helperFunctionOpenStage(String title, int sceneSizeW, int sceneSizeH, boolean ExitWindow, boolean PropertiesWindow, boolean WinWindow){        Stage stage = new Stage();        stage.setTitle(title);        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/"+title + ".fxml"));        Parent root = null;        try {            root = fxmlLoader.load();        } catch (IOException e) {            ErrorMessage("FXML loading problem");        }        Scene scene = new Scene(root, sceneSizeW, sceneSizeH);        stage.setScene(scene);        stage.setResizable(false);        stage.initModality(Modality.APPLICATION_MODAL); //lock the window        if(ExitWindow){ //if it's exit window            stage.setTitle(""); //delete title            ExitController exitController = fxmlLoader.getController();            Button cnl_btn = exitController.cancel_btn;            cnl_btn.setOnAction(e->{ //listen to cancel exit event ->                stage.close();  //return to the main stage                myStage.show();                e.consume();            });        }        if(PropertiesWindow){ //if it's properties window            PropertiesController propertiesController = fxmlLoader.getController();            Button ok_btn = propertiesController.OK_btn;            ok_btn.setOnAction(e->{ //listen to ok button press -> change configuration                mazeDisplayer.FalseDrawSolution(solution_btn); //hide solution                propertiesController.changeConfiguration(e); //change configuration                stage.close(); //close the properties stage                mazeDisplayer.drawMaze(); //draw maze again                e.consume();            });        }        if(WinWindow){ //if it's exit window            stage.setTitle(""); //delete title            WinController winController = fxmlLoader.getController();            Button restart_btn = winController.Restart;            Button exit_btn = winController.Exit;            restart_btn.setOnAction(e->{ //listen to restart game event ->                mazeDisplayer.restartSolutionPath();                viewModel.restartPlayer();                stage.close();                MusicChange();                e.consume();            });            exit_btn.setOnAction(e->{ //listen to exit game event ->                checkExitWanted();            });            stage.setOnCloseRequest(e->{ //listen to close event (user press on "x" key)                MusicChange();            });        }        stage.show();    }    public void MusicChange(){        int start = 0;        mp.stop();        mp = OldMp;        if(mp == GameMediaPlayer3){            currentSong.setText("Part Of Your World");            start = 9;        }        else if(mp == GameMediaPlayer2)            currentSong.setText("Kiss The Girl");        else            currentSong.setText("Under The Sea");        PlayerGameSoundTrack(start); //play WinMediaPlayer    }    //handle menu options -> open the right stages:    public void aboutWindow(ActionEvent actionEvent) {        helperFunctionOpenStage("About", 480,550, false, false, false);        actionEvent.consume(); }    public void helpWindow(ActionEvent actionEvent) {        helperFunctionOpenStage("Help", 750, 635, false, false, false);        actionEvent.consume();}    public void propertiesWindow(ActionEvent actionEvent) {        helperFunctionOpenStage("Properties", 400, 280, false, true, false);        actionEvent.consume(); }    public void checkExitWanted(){        helperFunctionOpenStage("Exit", 325, 180, true, false, false); }    public void exitGame(ActionEvent actionEvent) { //this function will handle the exit request        checkExitWanted();        actionEvent.consume();}    //load game from disk    public void loadGame(ActionEvent actionEvent) {        FileChooser fileChooser = createFileChooser("Open Maze Game");        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze Files", "*.maze"));        File chosenFile = fileChooser.showOpenDialog(myStage);        if (chosenFile == null){            return;}        try {            ObjectInputStream loadInputStream = new ObjectInputStream(new FileInputStream(chosenFile));            byte[] loadedMazeByteArray = (byte[]) loadInputStream.readObject();            Maze loadedMaze = new Maze(loadedMazeByteArray);            viewModel.setLoadedMaze(loadedMaze); //set the new maze to the model            textField_mazeRows.setText(String.valueOf(loadedMaze.getRows())); //update player position fields            textField_mazeColumns.setText(String.valueOf(loadedMaze.getColumns()));}        catch (Exception e) {            ErrorMessage("The maze could not be loaded, please select a different file");}        actionEvent.consume();}    //save game to disk    public void saveGame(ActionEvent actionEvent) {        if (viewModel.getMaze() == null) {            ErrorMessage("You must generate a maze before saving!");}        else{            FileChooser fileChooser = createFileChooser("Save Maze As");            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter( "Maze Files","*.maze"));            File file = fileChooser.showSaveDialog(myStage);            if (file == null) return;            ObjectOutputStream saveNewGame = null;            try {                saveNewGame = new ObjectOutputStream(new FileOutputStream(file));                saveNewGame.writeObject(viewModel.getMaze().toByteArray());                InformationMessage("your Game has been saved!");            } catch (Exception expectedException) {                ErrorMessage("you can't save your Game, try again!");}}        actionEvent.consume();    }    //open File Choose dialog    private FileChooser createFileChooser(String title){        FileChooser fileChooser = new FileChooser();        fileChooser.setTitle(title);        fileChooser.setInitialDirectory(new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()));        return fileChooser;}    //mouse dragged event    public void mouseDragged(MouseEvent mouseEvent) {        if (viewModel.getMaze() == null)            return;        //Cell Size        double cellHeight =mazeDisplayer.getCellHeight();        double cellWidth = mazeDisplayer.getCellWidth();        double addPlayerCol = mouseEvent.getX() - mouseX; //col        double addPlayerRow = mouseEvent.getY() - mouseY; // row        if (Math.abs(addPlayerCol) < cellWidth && Math.abs(addPlayerRow) < cellHeight) return;        int countRows = (int) (addPlayerRow / cellWidth);        int countCols = (int) (addPlayerCol / cellHeight);        if (countCols < 0)            viewModel.movePlayerByMouseDragged(Math.abs(countCols), KeyCode.LEFT);        if (countCols > 0)            viewModel.movePlayerByMouseDragged(Math.abs(countCols), KeyCode.RIGHT);        if (countRows < 0)            viewModel.movePlayerByMouseDragged(Math.abs(countRows), KeyCode.UP);        if (countRows > 0)            viewModel.movePlayerByMouseDragged(Math.abs(countRows), KeyCode.DOWN);        mouseEvent.consume();    }    public void mouseMoved(MouseEvent mouseEvent) {        mouseX = mouseEvent.getX();        mouseY = mouseEvent.getY();}    public String getPlayer(){ //get player from choosePlayer (user decision)        String player = (String) choosePlayer.getValue();        return player;}    public void changePlayer(ActionEvent actionEvent) { //update the player of the mazeDisplayer & draw again        mazeDisplayer.setPlayer(getPlayer());        mazeDisplayer.drawMaze();    }    public void setStageAndScene(Stage primaryStage) { //set the stage of this scene        myStage = primaryStage;    }    /*----------    Alert    ----------*/    private void InformationMessage(String message){        Alert alert = new Alert(Alert.AlertType.INFORMATION);        alert.setContentText(message);        alert.setTitle("");        alert.show(); }    private void ErrorMessage(String message){        Alert alert = new Alert(Alert.AlertType.ERROR);        alert.setContentText(message);        alert.setTitle("");        alert.show(); }    public void playMusic(MouseEvent mouseEvent) {        pausePressed= false;        mp.setVolume(1);        mp.play();        mp.setOnEndOfMedia(new Runnable() { //when the music over - start again            @Override            public void run() {                mp.seek(Duration.ZERO);}});    }    public void nextSong(MouseEvent mouseEvent) {        int start = 0;        OldMp = mp;        mp.stop();        if(mp==GameMediaPlayer){            mp = GameMediaPlayer2;            currentSong.setText("Kiss The Girl");        }        else if(mp == GameMediaPlayer2){            mp = GameMediaPlayer3;            start = 9;            currentSong.setText("Part Of Your World");        }        else{            mp = GameMediaPlayer;            currentSong.setText("Under The Sea");        }        PlayerGameSoundTrack(start);    }    public void pauseMusic(MouseEvent mouseEvent) {        pausePressed= true;        mp.pause();    }    /*------  option to see all Window in Zoom-Im ------*/    public void showRightWindow(){        if (viewModel.getMaze() == null) return;        if((3*mazeDisplayer.getHeight())/4 < mazeDisplayer.getDeltaX() + 10) return;        mazeDisplayer.setDeltaX(10);        mazeDisplayer.drawMaze();    }    public void showLeftWindow(){        if (viewModel.getMaze() == null) return;        if( 0 > mazeDisplayer.getDeltaX() - 10) return;        mazeDisplayer.setDeltaX(-10);        mazeDisplayer.drawMaze();    }    public void showUpWindow(){        if (viewModel.getMaze() == null) return;        if(0 > mazeDisplayer.getDeltaY() - 10) return;        mazeDisplayer.setDeltaY(-10);        mazeDisplayer.drawMaze();    }    public void showDownWindow(){        if (viewModel.getMaze() == null) return;        if((3*mazeDisplayer.getWidth())/4 < mazeDisplayer.getDeltaY() + 10) return;        mazeDisplayer.setDeltaY(10);        mazeDisplayer.drawMaze();    }}