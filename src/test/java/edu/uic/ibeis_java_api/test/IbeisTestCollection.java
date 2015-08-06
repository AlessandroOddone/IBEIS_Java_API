package edu.uic.ibeis_java_api.test;

import edu.uic.ibeis_java_api.api.*;
import edu.uic.ibeis_java_api.api.data.GeoCoordinates;
import edu.uic.ibeis_java_api.api.data.encounter.EncounterNotes;
import edu.uic.ibeis_java_api.api.data.image.ImageNotes;
import edu.uic.ibeis_java_api.api.data.image.RawImage;
import edu.uic.ibeis_java_api.api.data.individual.IndividualNotes;
import edu.uic.ibeis_java_api.values.Sex;
import edu.uic.ibeis_java_api.values.Species;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class IbeisTestCollection implements TestCollection {

    private Collection<Test> testCollection;
    private Ibeis ibeis;
    private List<IbeisImage> imageList;
    private List<IbeisAnnotation> annotationList;
    private List<IbeisIndividual> individualList;
    private List<IbeisEncounter> encounterList;
    IbeisImage image;
    IbeisAnnotation annotation1;
    IbeisAnnotation annotation2;
    IbeisIndividual individual;
    IbeisEncounter encounter;

    public IbeisTestCollection() {
        System.out.println("***IBEIS TEST COLLECTION***\n");
        testCollection = new ArrayList<>();
        ibeis = new Ibeis();

        try {
            imageList = ibeis.getAllImages();
            annotationList = ibeis.getAllAnnotations();
            individualList = ibeis.getAllIndividuals();
            encounterList = ibeis.getAllEncounters();

            image = ibeis.getImageById(179);
            annotation1 = ibeis.getAnnotationById(172);
            annotation2 = ibeis.getAnnotationById(174);
            individual = ibeis.getIndividualById(52);
            encounter = ibeis.getEncounterById(148);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * UPLOAD IMAGE TEST
         */
        //testCollection.add(new UploadImageTest(new File(getClass().getClassLoader().getResource("giraffe_api_upload_test.jpeg").getFile())));

        /**
         * UPLOAD IMAGE ZIP ARCHIVE TEST
         */
        //testCollection.add(new UploadImageZipArchiveTest(Arrays.asList(new File(getClass().getClassLoader().getResource("giraffe_api_upload_test.jpeg").getFile()),
                //new File(getClass().getClassLoader().getResource("zebra_api_upload_test.jpeg").getFile()))));

        /**
         * DELETE IMAGE TEST
         */
        //testCollection.add(new DeleteImagesTest(imageList.get(0)));

        /**
         * ADD INDIVIDUAL TEST
         */
        //testCollection.add(new AddIndividualTest(new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss_SSS").format(new Date())));

        /**
         * ADD ENCOUNTER TEST
         */
        //testCollection.add(new AddEncounterTest(new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss_SSS").format(new Date())));

        /**
         * DOWNLOAD IMAGE FILE TEST
         */

        //testCollection.add(new DownloadImageFileTest(imageList.get(imageList.size() - 1)));

        /**
         * ANIMAL DETECTION
         */
        //testCollection.add(new AnimalDetectionTest(imageList.get(imageList.size()-1), Species.GIRAFFE));

        /**
         * QUERY
         */
        //testCollection.add(new QueryTest(Arrays.asList(annotation1,annotation2),
        //        Arrays.asList(annotation1,annotation2)));

        /**
         * IMAGE GETTERS AND SETTERS
         */
        //testCollection.add(new ImageGettersAndSettersTest(imageList.get(imageList.size()-1)));

        /**
         * ANNOTATION GETTERS AND SETTERS
         */
        //testCollection.add(new AnnotationGettersAndSettersTest(annotationList.get(annotationList.size()-1)));

        /**
         * INDIVIDUAL GETTERS AND SETTERS
         */
        //testCollection.add(new IndividualGettersAndSettersTest(individualList.get(individualList.size()-1)));

        /**
         * ENCOUNTER GETTERS AND SETTERS
         */
        //testCollection.add(new EncounterGettersAndSettersTest(encounterList.get(encounterList.size()-1)));

    }

    public void runTests() {
        for(Test t : testCollection) {
            t.execute();
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }

    private class UploadImageTest implements Test {

        private File imageToUpload;
        private IbeisImage uploadedImage;

        public UploadImageTest(File file) {
            imageToUpload = file;
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            printTest(this);

            try {
                IbeisImage ibeisImage = ibeis.uploadImage(imageToUpload);

                System.out.println("image file \"" + imageToUpload.getPath() + "\" successfully uploaded (imageId = " + ibeisImage.getId() + ")");
                uploadedImage = ibeisImage;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public IbeisImage getUploadedImage() {
            return uploadedImage;
        }
    }

    private class UploadImageZipArchiveTest implements Test {

        private List<File> imagesToUpload;
        private List<IbeisImage> uploadedImages;

        public UploadImageZipArchiveTest(File file) {
            imagesToUpload = new ArrayList<>();
            imagesToUpload.add(file);
            uploadedImages = new ArrayList<>();
        }

        public UploadImageZipArchiveTest(List<File> files) {
            imagesToUpload = new ArrayList<>(files);
            uploadedImages = new ArrayList<>();
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            printTest(this);

            try {
                if (imagesToUpload.size() == 1) {
                    File imageFile = imagesToUpload.get(0);
                    IbeisImage ibeisImage = ibeis.uploadImage(imageFile);
                    System.out.println("image file \"" + imageFile.getPath() + "\" successfully uploaded (imageId = " + ibeisImage.getId() + ")");

                    uploadedImages.add(ibeisImage);
                }
                else {
                    List<IbeisImage> ibeisImageList = ibeis.uploadImages(imagesToUpload);
                    System.out.println("image files " + printFileList(imagesToUpload) +
                            " successfully uploaded (imageIds = [" + printIbeisImageList(ibeisImageList) + "])");

                    uploadedImages = ibeisImageList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public List<IbeisImage> getUploadedImages() {
            return uploadedImages;
        }
    }

    private class DeleteImagesTest implements Test {

        private List<IbeisImage> imagesToDelete;

        public DeleteImagesTest(IbeisImage image) {
            imagesToDelete = new ArrayList<>();
            imagesToDelete.add(image);
        }

        public DeleteImagesTest(List<IbeisImage> imageList) {
            imagesToDelete = new ArrayList<>(imageList);
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            printTest(this);

            try {
                if (imagesToDelete.size() == 1) {
                    ibeis.deleteImage(imagesToDelete.get(0));
                    System.out.println("image successfully deleted (imageId = " + imagesToDelete.get(0).getId() + ")");
                }
                else {
                    ibeis.deleteImages(imagesToDelete);
                    System.out.println("image files successfully deleted (imageIds = [" + printIbeisImageList(imagesToDelete) + "])");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AnimalDetectionTest implements Test {

        private List<IbeisImage> ibeisImageList;
        private Species species;
        private List<List<IbeisAnnotation>> ibeisAnnotationList;

        public AnimalDetectionTest(IbeisImage ibeisImage, Species species) {
            this.ibeisImageList = new ArrayList<>();
            this.ibeisImageList.add(ibeisImage);
            this.species = species;
        }

        public AnimalDetectionTest(List<IbeisImage> ibeisImageList, Species species) {
            this.ibeisImageList = new ArrayList<>(ibeisImageList);
            this.species = species;
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            printTest(this);
            try {
                ibeisAnnotationList = ibeis.runAnimalDetection(ibeisImageList, species);
                System.out.println("annotations list = " + printIbeisAnnotationList(ibeisAnnotationList));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public List<List<IbeisAnnotation>> getIbeisAnnotationList() {
            return ibeisAnnotationList;
        }
    }

    private class QueryTest implements Test {

        private List<IbeisAnnotation> queryAnnotations;
        private List<IbeisAnnotation> dbAnnotations;
        private List<IbeisQueryResult> ibeisQueryResults;

        public QueryTest(IbeisAnnotation queryAnnot, List<IbeisAnnotation> dbAnnots) {
            queryAnnotations = new ArrayList<>();
            queryAnnotations.add(queryAnnot);
            dbAnnotations = dbAnnots;
        }

        public QueryTest(List<IbeisAnnotation> queryAnnots, List<IbeisAnnotation> dbAnnots) {
            queryAnnotations = new ArrayList<>(queryAnnots);
            dbAnnotations = dbAnnots;
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            printTest(this);
            try {
                ibeisQueryResults = ibeis.query(queryAnnotations, dbAnnotations);
                for(int i=0; i<ibeisQueryResults.size(); i++) {
                    System.out.println("QUERY " + i + ": " + ibeisQueryResults.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AddIndividualTest implements Test {

        private String individualName;

        public AddIndividualTest(String individualName) {
            this.individualName = individualName;
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            printTest(this);
            try {
                IbeisIndividual testIndividual = ibeis.addIndividual(individualName);
                System.out.println("individual successfully added: " + testIndividual.getName() + " (id=" + testIndividual.getId() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AddEncounterTest implements Test {

        private String encounterName;

        public AddEncounterTest(String encounterName) {
            this.encounterName = encounterName;
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            printTest(this);
            try {
                IbeisEncounter testEncounter = ibeis.addEncounter(encounterName);
                System.out.println("encounter successfully added: " + testEncounter.getName() + " (id=" + testEncounter.getId() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadImageFileTest implements Test {

        private static final String DOWNLOADED_IMAGE_PATH = "src/test/resources/download_test.jpeg";

        private IbeisImage ibeisImage;

        public DownloadImageFileTest(IbeisImage ibeisImage) {
            this.ibeisImage = ibeisImage;
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            printTest(this);
            try {
                RawImage rawImage = ibeisImage.getRawImage();
                OutputStream outputStream = new FileOutputStream(DOWNLOADED_IMAGE_PATH);
                outputStream.write(rawImage.getBytes());
                System.out.println("image successfully uploaded to " + DOWNLOADED_IMAGE_PATH + " (file type: " +
                        rawImage.getFileType() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ImageGettersAndSettersTest implements Test {

        private IbeisImage ibeisImage;

        public ImageGettersAndSettersTest(IbeisImage ibeisImage) {
            this.ibeisImage = ibeisImage;
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            printTest(this);
            try {
                ibeisImage.setGpsPosition(new GeoCoordinates(10, 10));
                ibeisImage.setDatetime(new GregorianCalendar());
                ibeisImage.setNotes(new ImageNotes());
                ibeisImage.addToEncounter(encounterList.get(encounterList.size() - 1));

                System.out.println("location: " + ibeisImage.getGpsPosition());
                System.out.println("datetime: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ibeisImage.getDatetime().getTime()));
                System.out.println("size: " + ibeisImage.getSize());
                System.out.println("notes: " + ibeisImage.getNotes());
                System.out.println("annotations: " + printIbeisAnnotationListElement(ibeisImage.getAnnotations()));
                System.out.println("annotations by species (GIRAFFE): " + printIbeisAnnotationListElement
                        (ibeisImage.getAnnotationsOfSpecies(Species.GIRAFFE)));
                System.out.println("encounters: " + printIbeisEncounterList(ibeisImage.getEncounters()));
                System.out.println("is valid: " + ibeisImage.isValidImage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AnnotationGettersAndSettersTest implements Test {

        private IbeisAnnotation ibeisAnnotation;

        public AnnotationGettersAndSettersTest(IbeisAnnotation ibeisAnnotation) {
            this.ibeisAnnotation = ibeisAnnotation;
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            printTest(this);
            try {
                ibeisAnnotation.setIndividual(individualList.get(0));

                System.out.println("individual id: " + ibeisAnnotation.getIndividual().getId());
                System.out.println("bounding box: " + ibeisAnnotation.getBoundingBox());
                System.out.println("neighbor annotations: " + printIbeisAnnotationListElement(ibeisAnnotation.getNeighborAnnotations()));
                System.out.println("is valid: " + ibeisAnnotation.isValidAnnotation());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class IndividualGettersAndSettersTest implements Test {

        private IbeisIndividual ibeisIndividual;

        public IndividualGettersAndSettersTest(IbeisIndividual ibeisIndividual) {
            this.ibeisIndividual = ibeisIndividual;
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            printTest(this);
            try {
                ibeisIndividual.setName("Gianni");
                ibeisIndividual.setSex(Sex.MALE);
                ibeisIndividual.setIndividualNotes(new IndividualNotes());

                System.out.println("name: " + ibeisIndividual.getName());
                System.out.println("sex: " + ibeisIndividual.getSex().getValue());
                System.out.println("notes: " + ibeisIndividual.getNotes());
                System.out.println("images: " + printIbeisImageList(ibeisIndividual.getImages()));
                System.out.println("annotations: " + printIbeisAnnotationListElement(ibeisIndividual.getAnnotations()));
                System.out.println("is valid: " + ibeisIndividual.isValidIndividual());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class EncounterGettersAndSettersTest implements Test {

        private IbeisEncounter ibeisEncounter;

        public EncounterGettersAndSettersTest(IbeisEncounter ibeisEncounter) {
            this.ibeisEncounter = ibeisEncounter;
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            printTest(this);
            try {
                //ibeisEncounter.setName("test_encounter");
                ibeisEncounter.setNotes(new EncounterNotes());

                System.out.println("name: " + ibeisEncounter.getName());
                System.out.println("notes: " + ibeisEncounter.getNotes());
                System.out.println("images: " + printIbeisImageList(ibeisEncounter.getImages()));
                System.out.println("annotations: " + printIbeisIndividualList(ibeisEncounter.getIndividuals()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //Helper methods

    private static void printTest(Test test) {
        System.out.println("TEST: " + test.getTestType());
    }

    private String printFileList(List<File> files) {
        StringBuilder builder = new StringBuilder("");

        for(int i=0; i<files.size(); i++) {
            if(i < files.size()-1) {
                builder.append("\"" + files.get(i).getPath() + "\", ");
            }
            else {
                builder.append("\"" + files.get(i).getPath() + "\"");
            }
        }
        return builder.toString();
    }

    private String printIbeisImageList(List<IbeisImage> ibeisImageList) {
        StringBuilder builder = new StringBuilder("");

        for(int i=0; i<ibeisImageList.size(); i++) {
            if(i < ibeisImageList.size()-1) {
                builder.append(ibeisImageList.get(i).getId() + ", ");
            }
            else {
                builder.append(ibeisImageList.get(i).getId());
            }
        }
        return builder.toString();
    }

    private String printIbeisEncounterList(List<IbeisEncounter> ibeisEncounters) {
        StringBuilder builder = new StringBuilder("");

        for(int i=0; i<ibeisEncounters.size(); i++) {
            if(i < ibeisEncounters.size()-1) {
                builder.append(ibeisEncounters.get(i).getId() + ", ");
            }
            else {
                builder.append(ibeisEncounters.get(i).getId());
            }
        }
        return builder.toString();
    }

    private String printIbeisIndividualList(List<IbeisIndividual> ibeisIndividualList) {
        StringBuilder builder = new StringBuilder("");

        for(int i=0; i<ibeisIndividualList.size(); i++) {
            if(i < ibeisIndividualList.size()-1) {
                builder.append(ibeisIndividualList.get(i).getId() + ", ");
            }
            else {
                builder.append(ibeisIndividualList.get(i).getId());
            }
        }
        return builder.toString();
    }

    private String printIbeisAnnotationListElement(List<IbeisAnnotation> ibeisAnnotationElement) {
        StringBuilder builder = new StringBuilder("[");

        for(int i=0; i<ibeisAnnotationElement.size(); i++) {
            if(i < ibeisAnnotationElement.size()-1) {
                builder.append(ibeisAnnotationElement.get(i).getId() + ", ");
            }
            else {
                builder.append(ibeisAnnotationElement.get(i).getId());
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private String printIbeisAnnotationList(List<List<IbeisAnnotation>> ibeisAnnotationList) {
        StringBuilder builder = new StringBuilder("[");


        for(int i=0; i<ibeisAnnotationList.size(); i++) {
            if(i < ibeisAnnotationList.size()-1) {
                builder.append(printIbeisAnnotationListElement(ibeisAnnotationList.get(i)) + ", ");
            }
            else {
                builder.append(printIbeisAnnotationListElement(ibeisAnnotationList.get(i)));
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
