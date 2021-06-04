package View;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


import ViewModel.*;

/**
 * interface - responses for the representation of a model
 */

public interface IView {
    //public void setViewModel(MyViewModel viewModel);

    public String getUpdatePlayerRow();

    public void setUpdatePlayerRow(int row);

    public String getUpdatePlayerCol();

    public void setUpdatePlayerCol(int col);

    public void setPlayerPosition(int row, int col);

    //event handler - listen to key press event
    //keyEvent = argument of the event (we can use this to check with key is pressed)
    public void keyPressed(KeyEvent keyEvent);

    //focus on mouse click position

}
