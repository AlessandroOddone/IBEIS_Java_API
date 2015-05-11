package edu.uic.ibeis_java_api.test;

import edu.uic.ibeis_java_api.api.Ibeis;
import edu.uic.ibeis_java_api.api.IbeisImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class IbeisTestCollection implements TestCollection {

    private Collection<Test> testCollection;
    private Ibeis ibeis;

    public IbeisTestCollection() {
        System.out.println("***IBEIS TEST COLLECTION***\n");
        testCollection = new ArrayList<>();
        ibeis = new Ibeis();

        testCollection.add(new UploadImagesTest(new File(getClass().getClassLoader().getResource("zebra_api_upload_test.jpeg").getFile())));
        testCollection.add(new UploadImagesTest(Arrays.asList(new File(getClass().getClassLoader().getResource("zebra_api_upload_test.jpeg").getFile()),
                new File(getClass().getClassLoader().getResource("giraffe_api_upload_test.jpeg").getFile()))));
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

        public UploadImagesTest(File file) {
            imagesToUpload = new ArrayList<>();
            imagesToUpload.add(file);
        }

        public UploadImagesTest(List<File> files) {
            imagesToUpload = new ArrayList<>(files);
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
                }
                else {
                    List<IbeisImage> ibeisImageList = ibeis.uploadImages(imagesToUpload);
                    System.out.println(getTestType() + ": image files " + formatFilesList(imagesToUpload) +
                            " successfully uploaded (imageIds = [" + printIbeisImageIds(ibeisImageList) + "])");
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
    }
}
