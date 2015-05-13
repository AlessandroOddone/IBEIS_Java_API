package edu.uic.ibeis_java_api.test;

import edu.uic.ibeis_java_api.api.ImageZipArchive;
import edu.uic.ibeis_java_api.http.*;
import edu.uic.ibeis_java_api.values.Species;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HttpRequestTestCollection implements TestCollection {

    private Collection<HttpTest> testCollection;

    public HttpRequestTestCollection() {
        System.out.println("***HTTP REQUEST TEST COLLECTION***\n" );
        testCollection = new ArrayList<>();
        testCollection.add(new HttpGetTest("/image/"));
        //testCollection.add(new HttpGetTest("/image/unixtime/").addParam("gid_list", Arrays.asList(1, 2, 3, 35, 4, 5, 6, 1020)));

        /*
        Parameter imgZipArchive = new Parameter("image_zip_archive", getClass().getClassLoader().getResource("images_archive_test.zip").getFile());
        imgZipArchive.setIsFile(true);
        testCollection.add(new HttpPostTest("/image/").addParam(imgZipArchive));
        */

        testCollection.add(new HttpPutTest("/core/detect_random_forest/").addParam("gid_list", "119").addParam("species", Species.GIRAFFE.getValue()));
        testCollection.add(new HttpPutTest("/core/detect_random_forest/").addParam("gid_list", "119, 120").addParam("species", Species.GIRAFFE.getValue()));
        //testCollection.add(new HttpPutTest("/core/detect_random_forest/").addParam("gid_list", "199990").addParam("species", Species.ZEBRA_PLAIN.getValue()));
        //testCollection.add(new HttpPutTest("/core/query_all/").addParam("qaid_list", "1,2,3"));
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

    private static String formatParams(List<Parameter> params) {
        StringBuilder formattedParams = new StringBuilder("");
        for(int i=0; i<params.size(); i++) {
            formattedParams.append(params.get(i).getName() + "=" + params.get(i).getValue());
            if (i != params.size()-1) {
                formattedParams.append("; ");
            }
        }
        return formattedParams.toString();
    }

    private static void printResponse(Response response) {
        System.out.println("Response Success: " + response.isSuccess());
        System.out.println("Response Content: " + response.getContent().toString());
    }

    private class HttpGetTest implements HttpTest {

        private String callPath;
        private List<Parameter> params;

        public HttpGetTest(String callPath) {
            this.callPath = callPath;
            params = new ArrayList<>();
        }

        public String getCallPath() {
            return callPath;
        }

        public List<Parameter> getParams() {
            return params;
        }

        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        public HttpGetTest addParam(String name, String value) {
            params.add(new Parameter(name, value));
            return this;
        }

        public HttpGetTest addParam(String name, int value) {
            params.add(new Parameter(name, value));
            return this;
        }

        public HttpGetTest addParam(String name, List<Integer> values) {
            params.add(new Parameter(name, values));
            return this;
        }

        public HttpGetTest addParam(String name, ImageZipArchive imgZip) {
            params.add(new Parameter(name, imgZip));
            return this;
        }

        @Override
        public void execute() {
            printTest(this);

            try {
                if (params.size() > 0) {
                    printResponse(new Request(RequestMethod.GET, callPath, new ParametersList(params)).execute());
                }
                else {
                    printResponse(new Request(RequestMethod.GET, callPath).execute());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpPostTest implements HttpTest {

        private String callPath;
        private List<Parameter> params;

        public HttpPostTest(String callPath) {
            this.callPath = callPath;
            params = new ArrayList<>();
        }

        public String getCallPath() {
            return callPath;
        }

        public List<Parameter> getParams() {
            return params;
        }

        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        public HttpPostTest addParam(String name, String value) {
            params.add(new Parameter(name, value));
            return this;
        }

        public HttpPostTest addParam(String name, int value) {
            params.add(new Parameter(name, value));
            return this;
        }

        public HttpPostTest addParam(String name, List<Integer> values) {
            params.add(new Parameter(name, values));
            return this;
        }

        public HttpPostTest addParam(Parameter param) {
            params.add(param);
            return this;
        }

        @Override
        public void execute() {
            printTest(this);

            try {
                if (params.size() > 0) {
                    printResponse(new Request(RequestMethod.POST, callPath, new ParametersList(params)).execute());
                }
                else {
                    printResponse(new Request(RequestMethod.POST, callPath).execute());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpPutTest implements HttpTest {

        private String callPath;
        private List<Parameter> params;

        public HttpPutTest(String callPath) {
            this.callPath = callPath;
            params = new ArrayList<>();
        }

        public String getCallPath() {
            return callPath;
        }

        public List<Parameter> getParams() {
            return params;
        }

        public String getTestType() {
            return this.getClass().getSimpleName();
        }

        public HttpPutTest addParam(String name, String value) {
            params.add(new Parameter(name, value));
            return this;
        }

        public HttpPutTest addParam(String name, int value) {
            params.add(new Parameter(name, value));
            return this;
        }

        public HttpPutTest addParam(String name, List<Integer> values) {
            params.add(new Parameter(name, values));
            return this;
        }

        public HttpPutTest addParam(String name, ImageZipArchive imgZip) {
            params.add(new Parameter(name, imgZip));
            return this;
        }

        @Override
        public void execute() {
            printTest(this);

            try {
                if (params.size() > 0) {
                    printResponse(new Request(RequestMethod.PUT, callPath, new ParametersList(params)).execute());
                }
                else {
                    printResponse(new Request(RequestMethod.PUT, callPath).execute());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

