package edu.uic.ibeis_java_api.test.db_scripts;

import com.opencsv.CSVReader;
import edu.uic.ibeis_java_api.api.data.annotation.BoundingBox;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChipTable {

    private List<ChipTableEntry> tableEntries = new ArrayList<>();

    public ChipTable(File file) {
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
                int imageId = Integer.parseInt(nextLine[1].replaceAll("\\s",""));
                int nameId = Integer.parseInt(nextLine[2].replaceAll("\\s",""));

                StringBuilder builder = new StringBuilder(nextLine[3]);
                int x = Integer.parseInt(builder.substring(3,7).replaceAll("\\s",""));
                int y = Integer.parseInt(builder.substring(8,12).replaceAll("\\s",""));
                int w = Integer.parseInt(builder.substring(13,17).replaceAll("\\s",""));
                int h = Integer.parseInt(builder.substring(18,22).replaceAll("\\s",""));
                BoundingBox boundingBox = new BoundingBox(x,y,w,h);

                tableEntries.add(new ChipTableEntry(id,imageId,nameId,boundingBox));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableEntries;
    }

    public List<ChipTableEntry> getTableEntries() {
        return tableEntries;
    }
}
