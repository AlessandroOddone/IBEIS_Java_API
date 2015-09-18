package edu.uic.ibeis_java_api.test.db_scripts;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageTable {

    private List<ImageTableEntry> tableEntries = new ArrayList<>();

    public ImageTable(File file) {
        tableEntries = loadTable(file);
    }

    public List loadTable(File file) {

        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            String [] nextLine;

            // skip headers
            reader.readNext();
            reader.readNext();
            reader.readNext();

            while ((nextLine = reader.readNext()) != null) {
                int id = Integer.parseInt(nextLine[0].replaceAll("\\s",""));
                File filepath = new File(getClass().getClassLoader().getResource("BrookfieldZooGiraffesDB/images/" + nextLine[1].replaceAll("\\s", "")).getFile());

                tableEntries.add(new ImageTableEntry(id,filepath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableEntries;
    }

    public List<ImageTableEntry> getTableEntries() {
        return tableEntries;
    }
}
