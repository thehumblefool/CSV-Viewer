package application;

import csv.CSVFilter;
import csv.CSVReader;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.*;

public class Controller {

    @FXML
    private BorderPane mainWindow;

    @FXML
    private Button selectDirectory;

    @FXML
    private ComboBox<String> selectFile;

    @FXML
    private MenuButton selectColumns;

    @FXML
    private TableView<ObservableList<String>> table;

    private File directory;

    private String defaultPath;

    private String selectedFile;

    private Set<Integer> selectedColumns;

    private Map<String, Integer> columnsMap;

    private String[] header;

    public void initialize() {
        defaultPath = "/home/toor/Documents/CSVs/";
        directory = new File(defaultPath);
        columnsMap = new HashMap<>();
        selectedColumns = new TreeSet<>();
        selectedFile = null;
        header = null;
        selectFile.setTooltip(new Tooltip("Select a File"));
        selectDirectory.setTooltip(new Tooltip("Select a Folder"));
        loadFiles(directory);
    }

    private void loadFiles(File directory) {
        new Thread(() -> {
            String[] files = directory.list(new CSVFilter());
            if (files != null) {
                Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
                for (String file : files)
                    selectFile.getItems().add(file);
                selectFile.setPromptText("Select a file");
            }
        }).start();
    }


    @FXML
    public void loadFiles() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(directory);
        File path = directoryChooser.showDialog(mainWindow.getScene().getWindow());
        if(path!=null) {
            directory = path;
            selectFile.getItems().clear();
            selectedFile = null;
            selectColumns.getItems().clear();
            selectedColumns.clear();
            columnsMap.clear();
            header = null;
            loadFiles(directory);
        }
    }

    @FXML
    public void loadColumns() {
        selectColumns.getItems().clear();
        selectedFile = selectFile.getSelectionModel().getSelectedItem();
        if(directory==null|| selectedFile==null)
            return;
        File file = new File(directory, selectedFile);
        if(!file.exists())
            return;
        header = new CSVReader(file).getHeader();
        if(header != null) {
            new Thread( () -> {
                selectedColumns.clear();
                columnsMap.clear();
                for(int i=0; i<header.length; ++i) {
                    CheckBox checkBox = new CheckBox(header[i]);
                    columnsMap.put(header[i], i);
                    checkBox.selectedProperty().addListener( ((observable, oldValue, newValue) -> {
                        if(newValue) {
                            selectedColumns.add(columnsMap.get(checkBox.getText()));
                        } else {
                            selectedColumns.remove(columnsMap.get(checkBox.getText()));
                        }
                    }));
                    CustomMenuItem customMenuItem = new CustomMenuItem(checkBox);
                    customMenuItem.setHideOnClick(false);
                    selectColumns.getItems().add(customMenuItem);
                }

            }).start();
        }
    }

    @FXML
    public void loadData() {

        if(selectedFile==null)
            return;
        table.getColumns().clear();
        table.getItems().clear();
        int i=0;
        for(int num : selectedColumns) {
            final int index = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(header[num]);
            column.setCellValueFactory( param -> new ReadOnlyObjectWrapper<>(param.getValue().get(index)));
            table.getColumns().add(column);
            ++i;
        }

        new Thread( () -> new CSVReader(new File(directory, selectedFile)).loadDataInToTable(table, selectedColumns)).start();
    }
}
