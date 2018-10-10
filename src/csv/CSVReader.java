package csv;

import java.io.*;

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

}
