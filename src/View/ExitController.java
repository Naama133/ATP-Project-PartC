package View;

import ViewModel.MyViewModel;
import com.sun.glass.ui.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExitController implements Initializable {
    public Button cancel_btn; //todo dar
    public Button yes_Btn; //todo dar

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //todo
    }

    public void continueExit(ActionEvent actionEvent) {
        MyViewModel viewModel = MyViewModel.getInstance();
        viewModel.exitGame();
        System.exit(0);
    }

/*    public void abortExit(ActionEvent actionEvent) {
        //todo dar
        Stage stage = (Stage)cancel_btn.getScene().getWindow();
        stage.close();
        //((Node)(actionEvent.getSource())).getScene().getp;
       //todo
    }*/

}
