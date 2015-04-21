package test;

import edu.uic.api.Ibeis;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class IbeisTestCollection implements TestCollection {

    private Collection<Test> testCollection;
    private Ibeis ibeis;

    public IbeisTestCollection() {
        System.out.println("IBEIS TEST COLLECTION\n");
        testCollection = new ArrayList<>();
        ibeis = new Ibeis();

        //testCollection.add(new UploadImagesTest(new File("/home/alessandro/zebra_api_upload_test_alessandro.jpeg")));
        testCollection.add(new UploadImagesTest(Arrays.asList(new File("/home/alessandro/zebra_api_upload_test_alessandro.jpeg"),
                new File("/home/alessandro/giraffe_api_upload_test_alessandro.jpeg"))));
    }

    public void runTests() {
        for(Test t : testCollection) {
            t.execute();
        }
        System.out.print("\n\n");
    }

    private class UploadImagesTest implements Test{

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
                    int gId = ibeis.uploadImage(imageFile);
                    System.out.println(getTestType() + ": image file \"" + imageFile.getPath() + "\" successfully uploaded (gId = " + gId + ")");
                }
                else {
                    List<Integer> gIds = ibeis.uploadImages(imagesToUpload);
                    System.out.println(getTestType() + ": image files " + formatFilesList(imagesToUpload) +
                            "\" successfully uploaded (gIds = [" + formatIntegersList(gIds) + "])");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.print("\n");
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

        private String formatIntegersList(List<Integer> integerList) {
            StringBuilder builder = new StringBuilder("");

            for(int i=0; i<integerList.size(); i++) {
                if(i < integerList.size()-1) {
                    builder.append(integerList.get(i) + ", ");
                }
                else {
                    builder.append(integerList.get(i));
                }
            }
            return builder.toString();
        }
    }
}
