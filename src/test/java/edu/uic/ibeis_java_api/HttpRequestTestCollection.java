package edu.uic.ibeis_java_api;

import edu.uic.ibeis_java_api.api.image.ImageZipArchive;
import edu.uic.ibeis_java_api.exceptions.EmptyListParameterException;
import edu.uic.ibeis_java_api.http.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HttpRequestTestCollection implements TestCollection {

    private Collection<HttpTest> testCollection;

    public HttpRequestTestCollection() {
        System.out.println("***HTTP REQUEST TEST COLLECTION***\n");
        testCollection = new ArrayList<>();

        /**
         * IMAGE UPLOAD (IMAGE FILE)
         */
        //Parameter imageFile = new Parameter("image", getClass().getClassLoader().getResource("giraffe_api_upload_test.jpeg").getFile());
        //imageFile.setIsFile(true);
        //testCollection.add(new HttpPostTest("/image/").addParam(imageFile));

        /**
         * IMAGE UPLOAD (ZIP ARCHIVE)
         */
        //Parameter imgZipArchive = new Parameter("image_zip_archive", getClass().getClassLoader().getResource("images_archive_test_same_giraffe.zip").getFile());
        //imgZipArchive.setIsFile(true);
        //testCollection.add(new HttpPostTest("/image/zip/").addParam(imgZipArchive));

        /**
         * NAME INSERT
         */
        //testCollection.add(new HttpPostTest("/name/").addParam("name_text_list", "alessandro"));

        /**
         * ENCOUNTER INSERT
         */
        //testCollection.add(new HttpPostTest("/encounter/").addParam("enctext_list", "incontro"));

        /**
         * DETECT RANDOM FOREST
         */
        //testCollection.add(new HttpPutTest("/core/detect_random_forest/").addParam("gid_list", "180").addParam("species", Species.GIRAFFE.getValue()));
        //testCollection.add(new HttpPutTest("/core/detect_random_forest/").addParam("gid_list", "180,181").addParam("species", Species.GIRAFFE.getValue()));

        /**
         * QUERY
         */
        //testCollection.add(new HttpGetTest("/core/query_chips/").addParam("qaid_list", Arrays.asList(151, 152)).addParam("daid_list", Arrays.asList(151,152)));
        //testCollection.add(new HttpGetTest("/core/query_chips_simple_dict/").addParam("qaid_list", Arrays.asList(300)).addParam("daid_list", Arrays.asList(130)));

        /**
         * GETTERS (GET CALLS)
         */
        //testCollection.add(new HttpGetTest("/image/119/"));
        //testCollection.add(new HttpGetTest("/image/"));
        //testCollection.add(new HttpGetTest("/annot/"));
        //testCollection.add(new HttpGetTest("/name/"));
        //testCollection.add(new HttpGetTest("/encounter/"));

        /*
        testCollection.add(new HttpGetTest("/image/aids/").addParam("gid_list", Arrays.asList(151, 152)));
        testCollection.add(new HttpGetTest("/image/aids_of_species/")
                .addParam("gid_list", Arrays.asList(151,152))
                .addParam("species", Species.GIRAFFE.getValue()));
        testCollection.add(new HttpGetTest("/image/gps/").addParam("gid_list", Arrays.asList(151, 152)));
        testCollection.add(new HttpGetTest("/image/notes/").addParam("gid_list", Arrays.asList(151,152)));
        testCollection.add(new HttpGetTest("/image/sizes/").addParam("gid_list", Arrays.asList(151,152)));
        testCollection.add(new HttpGetTest("/image/unixtime/").addParam("gid_list", Arrays.asList(151,152)));
        testCollection.add(new HttpGetTest("/image/eids/").addParam("gid_list", Arrays.asList(151, 152)));
        */


        /*
        testCollection.add(new HttpGetTest("/annot/bboxes/").addParam("aid_list", Arrays.asList(2,3,4,5,7,8,10)));
        testCollection.add(new HttpGetTest("/annot/contact_aids/").addParam("aid_list", Arrays.asList(166,167,168,169,170,171)));
        testCollection.add(new HttpGetTest("/annot/name_rowids/").addParam("aid_list", Arrays.asList(166,167,168,169,170,171)));
        */

        /*
        testCollection.add(new HttpGetTest("/name/aids/").addParam("nid_list", Arrays.asList(-166,-167,-168,-169,-170,-171)));
        testCollection.add(new HttpGetTest("/name/gids/").addParam("nid_list", Arrays.asList(-166,-167,-168,-169,-170,-171)));
        testCollection.add(new HttpGetTest("/name/sex_text/").addParam("name_rowid_list", Arrays.asList(-166,-167,-168,-169,-170,-171)));
        testCollection.add(new HttpGetTest("/name/texts/").addParam("name_rowid_list", Arrays.asList(-166,-167,-168,-169,-170,-171)));
        testCollection.add(new HttpGetTest("/name/notes/").addParam("name_rowid_list", Arrays.asList(-166, -167, -168, -169, -170, -171)));
        */

        /*
        testCollection.add(new HttpGetTest("/encounter/aids/").addParam("eid_list", Arrays.asList(42)));
        testCollection.add(new HttpGetTest("/encounter/nids/").addParam("eid_list", Arrays.asList(34, 35)));
        testCollection.add(new HttpGetTest("/encounter/note/").addParam("eid_list", Arrays.asList(34,35)));
        testCollection.add(new HttpGetTest("/encounter/text/").addParam("eid_list", Arrays.asList(34,35)));
        */

        /**
         * SETTERS (PUT CALLS)
         */
        /*
        testCollection.add(new HttpPutTest("/image/gps/").addParam("gid_list", "151,152")
                .addParam("lat_list", Arrays.asList(41.931535, 41.931535)).addParam("lon_list", Arrays.asList(-87.711991, -87.711991)));
        testCollection.add(new HttpPutTest("/image/notes/").addParam("gid_list", "152").addParam("notes_list", "test_note"));
        testCollection.add(new HttpPutTest("/image/unixtime/").addParam("gid_list", "151,152").addParam("unixtime_list", Arrays.asList(1431642451, 1431642451)));
        testCollection.add(new HttpPutTest("/name/sex/").addParam("name_rowid_list", "-166,-167,-168,-169,-170,-171")
                .addParam("name_sex_list", Sex.MALE.getValue() + "," + Sex.FEMALE.getValue() + "," + Sex.UNKNOWN.getValue()
                        + "," + Sex.MALE.getValue() + "," + Sex.FEMALE.getValue() + "," + Sex.UNKNOWN.getValue()));
        testCollection.add(new HttpPutTest("/name/texts/").addParam("name_rowid_list", "-166")
                .addParam("name_text_list","test_name"));
        testCollection.add(new HttpPutTest("/name/notes/").addParam("name_rowid_list", "-166")
                .addParam("notes_list", "test_note"));
        testCollection.add(new HttpPutTest("/encounter/text/").addParam("eid_list", "34")
                .addParam("encounter_text_list", "encounter_name_test"));
        testCollection.add(new HttpPutTest("/encounter/notes/").addParam("encounter_rowid_list", "34")
                .addParam("encounter_note_list", "encounter_note_test"));
        testCollection.add(new HttpPutTest("/image/eids/").addParam("gid_list", "151")
                .addParam("eid_list", "34"));
        */

        /**
         * DELETE CALLS
         */
        /*
        List<Long> annotationsToDeleteIndexes = new ArrayList<>();
        List<Long> imagesToDeleteIndexes = new ArrayList<>();
        List<Long> encountersToDeleteIndexes = new ArrayList<>();
        List<Long> individualsToDeleteIndexes = new ArrayList<>();

        try {
            for (IbeisAnnotation ibeisAnnotation : new Ibeis().getAllAnnotations()) {
                annotationsToDeleteIndexes.add(ibeisAnnotation.getId());
            }
            for (IbeisImage ibeisImage : new Ibeis().getAllImages()) {
                imagesToDeleteIndexes.add(ibeisImage.getId());
            }
            for (IbeisEncounter ibeisEncounter : new Ibeis().getAllEncounters()) {
                encountersToDeleteIndexes.add(ibeisEncounter.getId());
            }
            for (IbeisIndividual ibeisIndividual : new Ibeis().getAllIndividuals()) {
                individualsToDeleteIndexes.add(ibeisIndividual.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        testCollection.add(new HttpDeleteTest("/annot/").addParam("aid_list", annotationsToDeleteIndexes));
        testCollection.add(new HttpDeleteTest("/name/").addParam("name_rowid_list", individualsToDeleteIndexes));
        testCollection.add(new HttpDeleteTest("/encounter/").addParam("eid_list", 5));
        testCollection.add(new HttpDeleteTest("/image/").addParam("gid_list", Arrays.asList(200)));
        */
    }

    public void runTests() {
        for(HttpTest t : testCollection) {
            t.execute();
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }

    private static void printTest(HttpTest test) {
        System.out.println("TEST: " + test.getTestType());
        System.out.println("Request path: " + test.getCallPath());
        System.out.println("Params: " + formatParams(test.getParams()));
    }

    private static String formatParams(List<HttpParameter> params) {
        StringBuilder formattedParams = new StringBuilder("");
        for(int i=0; i<params.size(); i++) {
            formattedParams.append(params.get(i).getName() + "=" + params.get(i).getValue());
            if (i != params.size()-1) {
                formattedParams.append("; ");
            }
        }
        return formattedParams.toString();
    }

    private static void printResponse(HttpResponse response) {
        System.out.println("Response Success: " + response.isSuccess());
        System.out.println("Response Content: " + response.getContent().toString());
    }

    private class HttpGetTest implements HttpTest {

        private String callPath;
        private List<HttpParameter> params;

        public HttpGetTest(String callPath) {
            this.callPath = callPath;
            params = new ArrayList<>();
        }

        public String getCallPath() {
            return callPath;
        }

        public List<HttpParameter> getParams() {
            return params;
        }

        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        public HttpGetTest addParam(String name, String value) {
            params.add(new HttpParameter(name, value));
            return this;
        }

        public HttpGetTest addParam(String name, Number value) {
            params.add(new HttpParameter(name, value));
            return this;
        }

        public HttpGetTest addParam(String name, List<? extends Number> values) throws EmptyListParameterException {
            params.add(new HttpParameter(name, values));
            return this;
        }

        public HttpGetTest addParam(String name, ImageZipArchive imgZip) {
            params.add(new HttpParameter(name, imgZip));
            return this;
        }

        @Override
        public void execute() {
            long startTime = System.nanoTime();
            printTest(this);

            try {
                if (params.size() > 0) {
                    printResponse(new HttpRequest(HttpRequestMethod.GET, callPath, new HttpParametersList(params)).execute());
                }
                else {
                    printResponse(new HttpRequest(HttpRequestMethod.GET, callPath).execute());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            long endTime = System.nanoTime();
            System.out.println("ELAPSED TIME: " + new DecimalFormat("#.###").format((double) (endTime - startTime)/1000000000) + " s");
        }
    }

    private class HttpPostTest implements HttpTest {

        private String callPath;
        private List<HttpParameter> params;

        public HttpPostTest(String callPath) {
            this.callPath = callPath;
            params = new ArrayList<>();
        }

        public String getCallPath() {
            return callPath;
        }

        public List<HttpParameter> getParams() {
            return params;
        }

        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        public HttpPostTest addParam(String name, String value) {
            params.add(new HttpParameter(name, value));
            return this;
        }

        public HttpPostTest addParam(String name, Number value) {
            params.add(new HttpParameter(name, value));
            return this;
        }

        public HttpPostTest addParam(String name, List<? extends Number> values) throws EmptyListParameterException {
            params.add(new HttpParameter(name, values));
            return this;
        }

        public HttpPostTest addParam(HttpParameter param) {
            params.add(param);
            return this;
        }

        @Override
        public void execute() {
            long startTime = System.nanoTime();
            printTest(this);

            try {
                if (params.size() > 0) {
                    printResponse(new HttpRequest(HttpRequestMethod.POST, callPath, new HttpParametersList(params)).execute());
                }
                else {
                    printResponse(new HttpRequest(HttpRequestMethod.POST, callPath).execute());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            long endTime = System.nanoTime();
            System.out.println("ELAPSED TIME: " + new DecimalFormat("#.###").format((double) (endTime - startTime)/1000000000) + " s");
        }
    }

    private class HttpPutTest implements HttpTest {

        private String callPath;
        private List<HttpParameter> params;

        public HttpPutTest(String callPath) {
            this.callPath = callPath;
            params = new ArrayList<>();
        }

        public String getCallPath() {
            return callPath;
        }

        public List<HttpParameter> getParams() {
            return params;
        }

        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        public HttpPutTest addParam(String name, String value) {
            params.add(new HttpParameter(name, value));
            return this;
        }

        public HttpPutTest addParam(String name, Number value) {
            params.add(new HttpParameter(name, value));
            return this;
        }

        public HttpPutTest addParam(String name, List<? extends Number> values) throws EmptyListParameterException {
            params.add(new HttpParameter(name, values));
            return this;
        }

        public HttpPutTest addParam(String name, ImageZipArchive imgZip) {
            params.add(new HttpParameter(name, imgZip));
            return this;
        }

        @Override
        public void execute() {
            long startTime = System.nanoTime();
            printTest(this);

            try {
                if (params.size() > 0) {
                    printResponse(new HttpRequest(HttpRequestMethod.PUT, callPath, new HttpParametersList(params)).execute());
                }
                else {
                    printResponse(new HttpRequest(HttpRequestMethod.PUT, callPath).execute());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            long endTime = System.nanoTime();
            System.out.println("ELAPSED TIME: " + new DecimalFormat("#.###").format((double) (endTime - startTime)/1000000000) + " s");
        }
    }

    private class HttpDeleteTest implements HttpTest {

        private String callPath;
        private List<HttpParameter> params;

        public HttpDeleteTest(String callPath) {
            this.callPath = callPath;
            params = new ArrayList<>();
        }

        public String getCallPath() {
            return callPath;
        }

        public List<HttpParameter> getParams() {
            return params;
        }

        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        public HttpDeleteTest addParam(String name, String value) {
            params.add(new HttpParameter(name, value));
            return this;
        }

        public HttpDeleteTest addParam(String name, Number value) {
            params.add(new HttpParameter(name, value));
            return this;
        }

        public HttpDeleteTest addParam(String name, List<? extends Number> values) throws EmptyListParameterException {
            params.add(new HttpParameter(name, values));
            return this;
        }

        public HttpDeleteTest addParam(String name, ImageZipArchive imgZip) {
            params.add(new HttpParameter(name, imgZip));
            return this;
        }

        @Override
        public void execute() {
            long startTime = System.nanoTime();
            printTest(this);

            try {
                if (params.size() > 0) {
                    printResponse(new HttpRequest(HttpRequestMethod.DELETE, callPath, new HttpParametersList(params)).execute());
                }
                else {
                    printResponse(new HttpRequest(HttpRequestMethod.DELETE, callPath).execute());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            long endTime = System.nanoTime();
            System.out.println("ELAPSED TIME: " + new DecimalFormat("#.###").format((double) (endTime - startTime)/1000000000) + " s");
        }
    }
}

