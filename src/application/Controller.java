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

    private List<CheckMenuItem> items;

    private Set<Integer> selectedColumns;

    private Map<String, Integer> columns;

    private String[] header;

    public void initialize() {
        defaultPath = "/home/toor/Documents/CSVs/";
        directory = new File(defaultPath);
        items = new ArrayList<>();
        columns = new HashMap<>();
        selectedColumns = new TreeSet<>();
        selectFile.setTooltip(new Tooltip("Select a File"));
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
            items.clear();
            selectedColumns.clear();
            columns.clear();
            loadFiles(directory);
        }
    }

    @FXML
    public void loadColumns() {
        selectColumns.getItems().clear();
        selectedFile = selectFile.getSelectionModel().getSelectedItem();
        File file = new File(directory, selectedFile);
        header = new CSVReader(file).getHeader();
        if(header != null) {
            items.clear();
            selectedColumns.clear();
            columns.clear();
            for(int i=0; i<header.length; ++i) {
                CheckMenuItem checkMenuItem = new CheckMenuItem(header[i]);
                columns.put(header[i], i);
                checkMenuItem.selectedProperty().addListener( ((observable, oldValue, newValue) -> {
                    if(newValue) {
                        selectedColumns.add(columns.get(checkMenuItem.getText()));
                    } else {
                        selectedColumns.remove(columns.get(checkMenuItem.getText()));
                    }
                }));
                items.add(checkMenuItem);
            }
            selectColumns.getItems().addAll(items);
        }
    }

    @FXML
    public void loadData() {
        table.getColumns().clear();
        int i=0;
        for(int num : selectedColumns) {
            final int index = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(header[num]);
            column.setCellValueFactory( param -> new ReadOnlyObjectWrapper<>(param.getValue().get(index)));
            table.getColumns().add(column);
            ++i;
        }
        CSVReader csvReader = new CSVReader(new File(directory, selectedFile));
        csvReader.loadDataInToTable(table, selectedColumns);
    }
}
