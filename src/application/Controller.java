package application;

import csv.CSVFilter;
import csv.CSVReader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class Controller {

    @FXML
    private BorderPane mainWindow;

    @FXML
    private Button selectDirectory;

    @FXML
    private ComboBox<String> selectFile;

    @FXML
    private ComboBox<CheckBox> selectColumns;

    private File directory;

    private String defaultPath;

    public void initialize() {
        defaultPath = "/home/toor/Documents/CSVs/";
        directory = new File(defaultPath);
        selectFile.setTooltip(new Tooltip("Select a file"));
        selectDirectory.setTooltip(new Tooltip("Select a Folder"));
        //selectColumms.setTooltip(new Tooltip("Select Columns"));
        loadFiles(directory);
    }

    private void loadFiles(File directory) {
        String[] files = directory.list(new CSVFilter());
        if(files != null) {
            for (int i = files.length - 1; i >= 0; --i)
                selectFile.getItems().add(files[i]);
            selectFile.setPromptText("Select a file");
        }
    }

    @FXML
    public void loadFiles() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(directory);
        File path = directoryChooser.showDialog(mainWindow.getScene().getWindow());
        if(path!=null) {
            directory = path;
            selectFile.getItems().clear();
            selectColumns.getItems().clear();
            loadFiles(directory);
        }
    }

    @FXML
    public void loadColumns() {
        selectColumns.getItems().clear();
        String selectedFile = selectFile.getSelectionModel().getSelectedItem();
        File file = new File(directory, selectedFile);
        String[] header = new CSVReader(file).getHeader();
        if(header != null) {
            for(int i=header.length-1; i>=0; --i) {
                selectColumns.getItems().add(new CheckBox(header[i]));
            }
        }
    }

    @FXML
    public void loadData() {
        System.out.println("Done");
        System.out.println(selectColumns.getSelectionModel().getSelectedItem());
    }
}
