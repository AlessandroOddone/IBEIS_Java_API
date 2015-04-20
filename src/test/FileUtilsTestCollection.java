package test;

import edu.uic.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileUtilsTestCollection {

    private Collection<Test> testCollection;

    public FileUtilsTestCollection() {
        System.out.println("FILE UTILS TEST COLLECTION\n");
        testCollection = new ArrayList<>();
        testCollection.add(new ZipFileTest(new File("/home/alessandro/giraffe_api_upload_test_alessandro.jpeg")));
        testCollection.add(new ZipFileTest(Arrays.asList(new File("/home/alessandro/zebra_api_upload_test_alessandro.jpeg"),
                new File("/home/alessandro/giraffe_api_upload_test_alessandro.jpeg"))));
    }

    public void runTests() {
        for(Test t : testCollection) {
            t.execute();
        }
        System.out.print("\n\n");
    }

    private class ZipFileTest implements Test{

        private List<File> filesToZip;

        public ZipFileTest(File file) {
            filesToZip = new ArrayList<>();
            filesToZip.add(file);
        }

        public ZipFileTest(List<File> files) {
            filesToZip = new ArrayList<>(files);
        }

        @Override
        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void execute() {
            File testArchivePath = new File("/home/alessandro/test_" + new SimpleDateFormat("MM-dd-yyyy_hh:mm:ss_SSS").format(new Date()) + ".zip");

            try {
                if (filesToZip.size() == 1) {
                    File file = filesToZip.get(0);
                    FileUtils.zipFile(file, testArchivePath);
                    System.out.println(getTestType() + ": file \"" + file.getPath() + "\" successfully zipped into archive \"" + testArchivePath + "\"");
                }
                else {
                    FileUtils.zipFiles(filesToZip, testArchivePath);
                    System.out.println(getTestType() + ": files " + formatFiles(filesToZip) + " successfully zipped into archive \"" + testArchivePath + "\"");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print("\n");
        }

        private String formatFiles(List<File> files) {
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
    }
}
