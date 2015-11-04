package edu.uic.ibeis_java_api.database_upload_tools.hotspotter;

import edu.uic.ibeis_java_api.api.*;
import edu.uic.ibeis_java_api.api.annotation.BoundingBox;
import edu.uic.ibeis_java_api.database_upload_tools.hotspotter.hotspotter_database_model.*;
import edu.uic.ibeis_java_api.exceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the upload of a Hotspotter database as an IBEIS encounter
 */
public class HotspotterDatabaseUploader {
    private ChipTable chipTable;
    private ImageTable imageTable;
    private NameTable nameTable;

    private Ibeis ibeis = new Ibeis();
    private String databaseName;
    private Collection<IbeisIndividual> individuals;

    private IbeisEncounter encounter;
    private HashMap<Integer,IbeisIndividual> ibeisIndividualHashMap;
    private HashMap<Integer,IbeisImage> ibeisImageHashMap;


    /**
     * Creates a HotspotterDatabaseUploader object. Individuals are added or retrieved based on their names only.
     * @param hotspotterDatabaseMainFolder main folder of Hotspotter database
     * @param databaseName name of the database/encounter
     */
    public HotspotterDatabaseUploader(File hotspotterDatabaseMainFolder, String databaseName) {
        this(hotspotterDatabaseMainFolder,databaseName,null);
    }

    /**
     * Creates a HotspotterDatabaseUploader object. Individuals are retrieved from a collection of individuals and matched with the names found in Hotspotter database.
     * @param hotspotterDatabaseMainFolder main folder of Hotspotter database
     * @param databaseName name of the database/encounter
     * @param individuals collection of individuals
     */
    public HotspotterDatabaseUploader(File hotspotterDatabaseMainFolder, String databaseName, Collection<IbeisIndividual> individuals) {
        this.databaseName = databaseName;
        this.individuals = individuals;
        this.chipTable = new ChipTable(new File(hotspotterDatabaseMainFolder.toString() + "/_hsdb/chip_table.csv"));
        this.imageTable = new ImageTable(new File(hotspotterDatabaseMainFolder.toString() + "/_hsdb/image_table.csv"));
        this.nameTable = new NameTable(new File(hotspotterDatabaseMainFolder.toString() + "/_hsdb/name_table.csv"));
    }

    public IbeisEncounter execute() throws UnsuccessfulHttpRequestException, IOException, UnsupportedImageFileTypeException {
        this.encounter = addOrGetEncounter(databaseName);
        this.ibeisIndividualHashMap = individuals == null ? addOrGetIndividuals() : addOrGetIndividuals(individuals);
        this.ibeisImageHashMap = uploadImages();
        addAnnotations();
        return this.encounter;
    }

    private IbeisEncounter addOrGetEncounter(String name) throws UnsuccessfulHttpRequestException, IOException {
        try {
            return ibeis.addEncounter(name);
        } catch (EncounterNameAlreadyExistsException e) {
            return findEncounter(name);
        } catch (MalformedHttpRequestException e) {
            e.printStackTrace();
        }
        return null;
    }

    private IbeisEncounter findEncounter(String name) throws UnsuccessfulHttpRequestException, IOException{
        try {
            List<IbeisEncounter> allEncounters = ibeis.getAllEncounters();
            for (IbeisEncounter encounter : allEncounters) {
                if (encounter.getName().equals(name)) {
                    return encounter;
                }
            }
        } catch (MalformedHttpRequestException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HashMap<Integer,IbeisIndividual> addOrGetIndividuals() throws UnsuccessfulHttpRequestException, IOException {
        ibeisIndividualHashMap = new HashMap<>();
        for (NameTableEntry nameTableEntry : nameTable.getTableEntries()) {
            int nameId = nameTableEntry.getId();
            String name = nameTableEntry.getName();
            try {
                ibeis.addIndividual(name);
            } catch (IndividualNameAlreadyExistsException e) {
                ibeisIndividualHashMap.put(nameId,findIndividual(name));
            } catch (MalformedHttpRequestException e) {
                e.printStackTrace();
            }
        }
        return ibeisIndividualHashMap;
    }

    private HashMap<Integer,IbeisIndividual> addOrGetIndividuals(Collection<IbeisIndividual> individuals) throws UnsuccessfulHttpRequestException, IOException {
        ibeisIndividualHashMap = new HashMap<>();
        for (NameTableEntry nameTableEntry : nameTable.getTableEntries()) {
            int nameId = nameTableEntry.getId();
            String name = nameTableEntry.getName();
            for (IbeisIndividual individual : individuals) {
                try {
                    if (name.equals(individual.getName())) {
                        ibeisIndividualHashMap.put(nameId,individual);
                        break;
                    }
                } catch (MalformedHttpRequestException e) {
                    e.printStackTrace();
                }
            }
        }
        return ibeisIndividualHashMap;
    }


    private IbeisIndividual findIndividual(String name) throws UnsuccessfulHttpRequestException, IOException {
        try {
            List<IbeisIndividual> allIndividuals = ibeis.getAllIndividuals();
            for (IbeisIndividual individual : allIndividuals) {
                if (individual.getName().equals(name)) {
                    return individual;
                }
            }
        } catch (MalformedHttpRequestException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HashMap<Integer,IbeisImage> uploadImages() throws UnsupportedImageFileTypeException, UnsuccessfulHttpRequestException, IOException {
        ibeisImageHashMap = new HashMap<>();

        for (ImageTableEntry imageTableEntry : imageTable.getTableEntries()) {
            IbeisImage ibeisImage;
            try {
                ibeisImage = ibeis.uploadImage(imageTableEntry.getFilepath());
                ibeisImage.addToEncounter(encounter);
                ibeisImageHashMap.put(imageTableEntry.getId(),ibeisImage);
            } catch (MalformedHttpRequestException e) {
                e.printStackTrace();
            }
        }
        return  ibeisImageHashMap;
    }

    private void addAnnotations() throws UnsuccessfulHttpRequestException, IOException {
        try {
            for (ChipTableEntry chipTableEntry : chipTable.getTableEntries()) {
                IbeisImage image = ibeisImageHashMap.get(chipTableEntry.getImageId());
                Collection<BoundingBox> imageAnnotationBoundingBoxes = new ArrayList<>();
                for (IbeisAnnotation annotation : image.getAnnotations()) {
                    imageAnnotationBoundingBoxes.add(annotation.getBoundingBox());
                }
                if (!imageAnnotationBoundingBoxes.contains(chipTableEntry.getBoundingBox())) {
                    ibeis.addAnnotation(image,chipTableEntry.getBoundingBox())
                            .setIndividual(ibeisIndividualHashMap.get(chipTableEntry.getNameId()));
                }
            }
        } catch (MalformedHttpRequestException e) {
            e.printStackTrace();
        }
    }
}
