package csv;

import java.io.File;
import java.io.FilenameFilter;

public class CSVFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".csv");
    }
}
