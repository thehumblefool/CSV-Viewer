package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {

    @FXML
    private GridPane mainWindow;

    @FXML
    private Button open;

    public void initialize() {

    }

    @FXML
    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(mainWindow.getScene().getWindow());
        if(file!=null) {
            System.out.println(file.getPath());

        }
    }

}
