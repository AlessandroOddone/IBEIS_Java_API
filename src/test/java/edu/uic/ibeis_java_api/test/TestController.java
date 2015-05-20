package edu.uic.ibeis_java_api.test;

public class TestController {

    public static void main(String[] args) {
        new HttpRequestTestCollection().runTests();
        //new FileUtilsTestCollection().runTests();
        new IbeisTestCollection().runTests();
    }
}
