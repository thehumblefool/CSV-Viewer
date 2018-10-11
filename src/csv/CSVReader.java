package csv;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Set;

public class CSVReader {

    private File file;

    public CSVReader(File file) {
        this.file = file;
    }

    public String[] getHeader() {

        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            if(header==null)
                return null;
            return header.split(",");

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    public void loadDataInToTable(TableView<ObservableList<String>> table, Set<Integer> columns) {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String data = reader.readLine();
            while((data=reader.readLine()) != null) {
                ObservableList<String> row = FXCollections.observableArrayList();
                String[] rowData = data.split(",");
                for(int column : columns)
                    row.add(rowData[column]);
                table.getItems().add(row);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

}
