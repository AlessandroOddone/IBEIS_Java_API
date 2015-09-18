package edu.uic.ibeis_java_api.test.db_scripts;

import edu.uic.ibeis_java_api.api.Ibeis;
import edu.uic.ibeis_java_api.api.IbeisEncounter;
import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.api.IbeisIndividual;
import edu.uic.ibeis_java_api.api.data.individual.IndividualNotes;
import edu.uic.ibeis_java_api.api.data.individual.Size;
import edu.uic.ibeis_java_api.api.data.individual.Weight;
import edu.uic.ibeis_java_api.exceptions.BadHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.IndividualNameAlreadyExistsException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.values.ConservationStatus;
import edu.uic.ibeis_java_api.values.LengthUnitOfMeasure;
import edu.uic.ibeis_java_api.values.Sex;
import edu.uic.ibeis_java_api.values.WeightUnitOfMeasure;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class BrookfieldZooGiraffesDbScript {

    private static ChipTable chipTable;
    private static ImageTable imageTable;
    private static NameTable nameTable;

    private static Ibeis ibeis = new Ibeis();

    private static final String ENCOUNTER_NAME = "Brookfield Zoo Giraffes Db";
    private static final String ARNIETA = "Arnieta";
    private static final String FRANNY = "Franny";
    private static final String JASIRI = "Jasiri";
    private static final String MITHRA = "Mithra";
    private static final String POTOKA = "Potoka";

    private static IbeisEncounter encounter;
    private static IbeisIndividual arnieta;
    private static IbeisIndividual franny;
    private static IbeisIndividual jasiri;
    private static IbeisIndividual mithra;
    private static IbeisIndividual potoka;

    private static HashMap<Integer,IbeisImage> ibeisImageHashMap = new HashMap<>();
    private static HashMap<Integer,IbeisIndividual> ibeisIndividualHashMap = new HashMap<>();

    public static void main (String[] args) {

        //load db tables
        chipTable = new ChipTable(new File(BrookfieldZooGiraffesDbScript.class.getClassLoader().getResource("BrookfieldZooGiraffesDB/_hsdb/chip_table.csv").getFile()));
        imageTable = new ImageTable(new File(BrookfieldZooGiraffesDbScript.class.getClassLoader().getResource("BrookfieldZooGiraffesDB/_hsdb/image_table.csv").getFile()));
        nameTable = new NameTable(new File(BrookfieldZooGiraffesDbScript.class.getClassLoader().getResource("BrookfieldZooGiraffesDB/_hsdb/name_table.csv").getFile()));

        addEncounter();
        addIndividuals();
        addImages();
        addAnnotations();
    }

    private static void addEncounter() {
        try {
            encounter = ibeis.addEncounter(ENCOUNTER_NAME);
        } catch (IOException | BadHttpRequestException | UnsuccessfulHttpRequestException e) {
            e.printStackTrace();
        } catch (IndividualNameAlreadyExistsException e) {
            encounter = findEncounter(ENCOUNTER_NAME);
        } finally {
            System.out.println("ENCOUNTER ID: " + encounter.getId());
        }
    }

    private static IbeisEncounter findEncounter(String name) {
        try {
            List<IbeisEncounter> allEncounters = ibeis.getAllEncounters();
            for (IbeisEncounter encounter : allEncounters) {
                if (encounter.getName().equals(name)) {
                    return encounter;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void addIndividuals() {
        try {
            //Arnieta
            try {
                arnieta = ibeis.addIndividual(ARNIETA);
                arnieta.setSex(Sex.FEMALE);
                IndividualNotes ArnietaNotes = new IndividualNotes();
                ArnietaNotes.setDescription("Arnieta is the most social with behind the scenes tours and guests. " +
                        "This \"Mamma\'s Girl\" likes to know where her mother, Franny, is at all times.");
                ArnietaNotes.setLocation("Brookfield Zoo");
                ArnietaNotes.setHabitat("Habitat Africa! The Savannah");
                ArnietaNotes.setConservationStatus(ConservationStatus.LC);
                ArnietaNotes.setDateOfBirth(new GregorianCalendar(2005, 7, 12).getTime());
                ArnietaNotes.setSize(new Size(13.5, LengthUnitOfMeasure.FOOT));
                ArnietaNotes.setWeight(new Weight(1860, WeightUnitOfMeasure.POUND));
                ArnietaNotes.setDiet("Alfalfa hay, grain, chopped carrots, sweet potatoes, apples, and bread;" +
                        " willow and maple browse when available.");
                arnieta.setIndividualNotes(ArnietaNotes);
            } catch (IndividualNameAlreadyExistsException e) {
                arnieta = findIndividual(ARNIETA);
            }

            //Franny
            try {
                franny = ibeis.addIndividual(FRANNY);
                franny.setSex(Sex.FEMALE);
                IndividualNotes FrannyNotes = new IndividualNotes();
                FrannyNotes.setDescription("Franny likes to stand out in the rain. She will wait for the weight of the water" +
                        " to lower leaves so she can reach them. She is the least social giraffe with her keepers.");
                FrannyNotes.setLocation("Brookfield Zoo");
                FrannyNotes.setHabitat("Habitat Africa! The Savannah");
                FrannyNotes.setConservationStatus(ConservationStatus.LC);
                FrannyNotes.setDateOfBirth(new GregorianCalendar(1991, 7, 21).getTime());
                FrannyNotes.setSize(new Size(14.5, LengthUnitOfMeasure.FOOT));
                FrannyNotes.setWeight(new Weight(1760, WeightUnitOfMeasure.POUND));
                FrannyNotes.setDiet("Alfalfa hay, grain, chopped carrots, sweet potatoes, apples, and bread;" +
                        " willow and maple browse when available.");
                franny.setIndividualNotes(FrannyNotes);
            } catch (IndividualNameAlreadyExistsException e) {
                franny = findIndividual(FRANNY);
            }

            //Jasiri
            try {
                jasiri = ibeis.addIndividual(JASIRI);
                jasiri.setSex(Sex.FEMALE);
                IndividualNotes JasiriNotes = new IndividualNotes();
                JasiriNotes.setDescription("Jasiri, mother to Potaka, hates to be rained on." +
                        " She will ask to come inside when it is raining." +
                        " She investigates items on the ground more than the other giraffes." +
                        " Her nickname is Jazzy.");
                JasiriNotes.setLocation("Brookfield Zoo");
                JasiriNotes.setHabitat("Habitat Africa! The Savannah");
                JasiriNotes.setConservationStatus(ConservationStatus.LC);
                JasiriNotes.setDateOfBirth(new GregorianCalendar(2005, 7, 12).getTime());
                JasiriNotes.setSize(new Size(13, LengthUnitOfMeasure.FOOT));
                JasiriNotes.setWeight(new Weight(1860, WeightUnitOfMeasure.POUND));
                JasiriNotes.setDiet("Alfalfa hay, grain, chopped carrots, sweet potatoes, apples, and bread;" +
                        " willow and maple browse when available.");
                jasiri.setIndividualNotes(JasiriNotes);
            } catch (IndividualNameAlreadyExistsException e) {
                jasiri = findIndividual(JASIRI);
            }

            //Mithra
            try {
                mithra = ibeis.addIndividual(MITHRA);
                mithra.setSex(Sex.FEMALE);
                IndividualNotes MithraNotes = new IndividualNotes();
                MithraNotes.setDescription("Mithra is our most gentile giraffe. She has a very sweet disposition, expressive face, ears." +
                        " She, also, seems to forget the tortoises over each winter and is re-surprised by them every spring.");
                MithraNotes.setLocation("Brookfield Zoo");
                MithraNotes.setHabitat("Habitat Africa! The Savannah");
                MithraNotes.setConservationStatus(ConservationStatus.LC);
                MithraNotes.setDateOfBirth(new GregorianCalendar(1990, 5, 23).getTime());
                MithraNotes.setSize(new Size(14, LengthUnitOfMeasure.FOOT));
                MithraNotes.setWeight(new Weight(1694, WeightUnitOfMeasure.POUND));
                MithraNotes.setDiet("Alfalfa hay, grain, chopped carrots, sweet potatoes, apples, and bread;" +
                        " willow and maple browse when available.");
                mithra.setIndividualNotes(MithraNotes);
            } catch (IndividualNameAlreadyExistsException e) {
                mithra = findIndividual(MITHRA);
            }

            //Potoka
            try {
                potoka = ibeis.addIndividual(POTOKA);
                potoka.setSex(Sex.FEMALE);
                IndividualNotes PotokaNotes = new IndividualNotes();
                PotokaNotes.setDescription("Potoka is our youngest and fastest growing giraffe." +
                        " This laid back male can be frequently seen \"splay-legged\" grazing on grass in his outdoor habitat.");
                PotokaNotes.setLocation("Brookfield Zoo");
                PotokaNotes.setHabitat("Habitat Africa! The Savannah");
                PotokaNotes.setConservationStatus(ConservationStatus.LC);
                PotokaNotes.setDateOfBirth(new GregorianCalendar(2013, 6, 21).getTime());
                PotokaNotes.setSize(new Size(12, LengthUnitOfMeasure.FOOT));
                PotokaNotes.setWeight(new Weight(1320, WeightUnitOfMeasure.POUND));
                PotokaNotes.setDiet("Alfalfa hay, grain, chopped carrots, sweet potatoes, apples, and bread;" +
                        " willow and maple browse when available.");
                potoka.setIndividualNotes(PotokaNotes);
            } catch (IndividualNameAlreadyExistsException e) {
                potoka = findIndividual(POTOKA);
            }
        } catch (IOException | BadHttpRequestException | UnsuccessfulHttpRequestException e) {
            e.printStackTrace();
        } finally {
            for (NameTableEntry nameTableEntry : nameTable.getTableEntries()) {
                int id = nameTableEntry.getId();
                String name = nameTableEntry.getName();

                if (name.equals(ARNIETA)) {
                    ibeisIndividualHashMap.put(id,arnieta);
                }
                else if (name.equals(FRANNY)) {
                    ibeisIndividualHashMap.put(id,franny);
                }
                else if (name.equals(JASIRI)) {
                    ibeisIndividualHashMap.put(id,jasiri);
                }
                else if (name.equals(MITHRA)) {
                    ibeisIndividualHashMap.put(id,mithra);
                }
                else if (name.equals(POTOKA)) {
                    ibeisIndividualHashMap.put(id,potoka);
                }
            }
        }
    }

    private static IbeisIndividual findIndividual(String name) {
        try {
            List<IbeisIndividual> allIndividuals = ibeis.getAllIndividuals();
            for (IbeisIndividual individual : allIndividuals) {
                if (individual.getName().equals(name)) {
                    return individual;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void addImages() {
        try {
            for (ImageTableEntry imageTableEntry : imageTable.getTableEntries()) {
                IbeisImage ibeisImage = ibeis.uploadImage(imageTableEntry.getFilepath());
                ibeisImage.addToEncounter(encounter);
                ibeisImageHashMap.put(imageTableEntry.getId(), ibeisImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addAnnotations() {
        try {
            for (ChipTableEntry chipTableEntry : chipTable.getTableEntries()) {
                ibeis.addAnnotation(ibeisImageHashMap.get(chipTableEntry.getImageId()),chipTableEntry.getBoundingBox())
                        .setIndividual(ibeisIndividualHashMap.get(chipTableEntry.getNameId()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
