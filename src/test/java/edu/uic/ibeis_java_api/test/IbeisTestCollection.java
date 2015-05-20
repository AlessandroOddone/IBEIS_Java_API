package edu.uic.ibeis_java_api.test;

import edu.uic.ibeis_java_api.api.Ibeis;
import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.api.IbeisIndividual;
import edu.uic.ibeis_java_api.exceptions.BadHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.values.Species;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class IbeisTestCollection implements TestCollection {

    private Collection<Test> testCollection;
    private Ibeis ibeis;

    public IbeisTestCollection() {
        System.out.println("***IBEIS TEST COLLECTION***\n");
        testCollection = new ArrayList<>();
        ibeis = new Ibeis();

        /**
         * GET ALL IMAGES
         */
        /*
        try {
            List<IbeisImage> ibeisImages = ibeis.getAllImages();
            printIbeisImageList(ibeisImages);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
        } catch (UnsuccessfulHttpRequestException e) {
            e.printStackTrace();
        }
        */

        /**
         * GET ALL INDIVIDUALS
         */
        /*
        try {
            List<IbeisIndividual> ibeisIndividuals = ibeis.getAllIndividuals();
            printIbeisIndividualList(ibeisIndividuals);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
        } catch (UnsuccessfulHttpRequestException e) {
            e.printStackTrace();
        }
        */

        /**
         * UPLOAD IMAGE
         */

        UploadImagesTest uploadTest = new UploadImagesTest(Arrays.asList(new File(getClass().getClassLoader().getResource("zebra_api_upload_test.jpeg").getFile()),
                new File(getClass().getClassLoader().getResource("giraffe_api_upload_test_2.jpeg").getFile())));
        uploadTest.execute();
        System.out.println("\n");

        /**
         * DELETE IMAGE
         */
        DeleteImagesTest deleteTest = new DeleteImagesTest(uploadTest.getUploadedImages().get(0));
        deleteTest.execute();

        /**
         * VALID IMAGE
         */
        try {
            System.out.println("isValidImage: " + uploadTest.getUploadedImages().get(0).isValidImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * DOWNLOAD IMAGE FILE
         */
        /*
        try {
            IbeisImage testImage = uploadTest.getUploadedImages().get(0);
            RawImage rawImage = testImage.getRawImage();
            System.out.println("file type: " + rawImage.getFileType());
            try (OutputStream stream = new FileOutputStream("src/test/resources/download_test.jpeg")) {
                stream.write(rawImage.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        */


        /**
         * IMAGE GETTERS
         */
        try {
            IbeisImage testImage = uploadTest.getUploadedImages().get(0);
            //System.out.println("Image location: " + testImage.getLocation());
            //System.out.println("Image datetime: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testImage.getDatetime().getTime()));
            //System.out.println("Image size: " + testImage.getSize());
            //System.out.println("Image note: " + testImage.getNote());
            System.out.println("Image annotations: " + printIbeisAnnotationListElement(testImage.getAnnotations()));
            System.out.println("Image annotations by species (GIRAFFE): " + printIbeisAnnotationListElement
                    (testImage.getAnnotationsOfSpecies(Species.GIRAFFE)));
            System.out.println("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * ANIMAL DETECTION
         */
        /*
        AnimalDetectionTest animalDetectionTest = new AnimalDetectionTest(uploadTest.getUploadedImages(), Species.GIRAFFE);
        animalDetectionTest.execute();
        System.out.println("\n");
        */

        /**
         * ANNOTATION GETTERS
         */
        /*
        for (IbeisAnnotation annot : animalDetectionTest.getIbeisAnnotationList().get(0)) {
            try {
                System.out.println("Individual id: " + annot.getIndividual().getId());
                System.out.println("Bounding Box: " + annot.getBoundingBox());
                System.out.println("Neighbor Annotations: " + printIbeisAnnotationListElement(annot.getNeighborAnnotations()));
                System.out.println("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("\n");
        }
        */

        /**
         * INDIVIDUAL GETTERS
         */
        /*
        try {
            IbeisIndividual testIndividual = animalDetectionTest.getIbeisAnnotationList().get(0).get(0).getIndividual();
            System.out.println("Individual name: " + testIndividual.getName());
            System.out.println("Individual sex: " + testIndividual.getSex().getValue());
            System.out.println("Individual annotations: " + printIbeisAnnotationListElement(testIndividual.getAnnotations()));
            System.out.println("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    public void runTests() {
        for(Test t : testCollection) {
            t.execute();
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }

    private static void printTest(Test test) {
        System.out.println("TEST: " + test.getTestType());
    }

    private class UploadImagesTest implements Test {

        private List<File> imagesToUpload;
        private List<IbeisImage> uploadedImages;

        public UploadImagesTest(File file) {
            imagesToUpload = new ArrayList<>();
            imagesToUpload.add(file);
            uploadedImages = new ArrayList<>();
        }

        public UploadImagesTest(List<File> files) {
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

            } catch (IOException e) {
                e.printStackTrace();
            } catch (BadHttpRequestException e) {
                e.printStackTrace();
            } catch (UnsuccessfulHttpRequestException e) {
                e.printStackTrace();
            }
        }

        public List<List<IbeisAnnotation>> getIbeisAnnotationList() {
            return ibeisAnnotationList;
        }
    }

    public String printFileList(List<File> files) {
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

    public String printIbeisImageList(List<IbeisImage> ibeisImageList) {
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

    public String printIbeisIndividualList(List<IbeisIndividual> ibeisIndividualList) {
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

    public String printIbeisAnnotationListElement(List<IbeisAnnotation> ibeisAnnotationElement) {
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

    public String printIbeisAnnotationList(List<List<IbeisAnnotation>> ibeisAnnotationList) {
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
