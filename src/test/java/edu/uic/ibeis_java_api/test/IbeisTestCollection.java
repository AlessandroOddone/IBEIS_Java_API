package edu.uic.ibeis_java_api.test;

import edu.uic.ibeis_java_api.api.Ibeis;
import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.values.Species;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class IbeisTestCollection implements TestCollection {

    private Collection<Test> testCollection;
    private Ibeis ibeis;

    public IbeisTestCollection() {
        System.out.println("***IBEIS TEST COLLECTION***\n");
        testCollection = new ArrayList<>();
        ibeis = new Ibeis();

        UploadImagesTest uploadTest = new UploadImagesTest(Arrays.asList(new File(getClass().getClassLoader().getResource("zebra_api_upload_test.jpeg").getFile())));
        uploadTest.execute();

        AnimalDetectionTest animalDetectionTest = new AnimalDetectionTest(uploadTest.getUploadedImages(), Species.GIRAFFE);
        animalDetectionTest.execute();
    }

    public void runTests() {
        for(Test t : testCollection) {
            t.execute();
            System.out.print("\n");
        }
        System.out.print("\n\n");
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
            File testArchivePath = new File("/home/alessandro/test_" + new SimpleDateFormat("MM-dd-yyyy_hh:mm:ss_SSS").format(new Date()) + ".zip");

            try {
                if (imagesToUpload.size() == 1) {
                    File imageFile = imagesToUpload.get(0);
                    IbeisImage ibeisImage = ibeis.uploadImage(imageFile);
                    System.out.println(getTestType() + ": image file \"" + imageFile.getPath() + "\" successfully uploaded (imageId = " + ibeisImage.getId() + ")");

                    uploadedImages.add(ibeisImage);
                }
                else {
                    List<IbeisImage> ibeisImageList = ibeis.uploadImages(imagesToUpload);
                    System.out.println(getTestType() + ": image files " + formatFilesList(imagesToUpload) +
                            " successfully uploaded (imageIds = [" + printIbeisImageIds(ibeisImageList) + "])");

                    uploadedImages = ibeisImageList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String formatFilesList(List<File> files) {
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

        private String printIbeisImageIds(List<IbeisImage> ibeisImageList) {
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

        public List<IbeisImage> getUploadedImages() {
            return uploadedImages;
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
            try {
                ibeisAnnotationList = ibeis.runAnimalDetection(ibeisImageList, species);
                System.out.println(getTestType() + ": annotations list = " + printIbeisAnnotationList(ibeisAnnotationList));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsuccessfulHttpRequestException e) {
                e.printStackTrace();
            }
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

        public List<List<IbeisAnnotation>> getIbeisAnnotationList() {
            return ibeisAnnotationList;
        }
    }


}
