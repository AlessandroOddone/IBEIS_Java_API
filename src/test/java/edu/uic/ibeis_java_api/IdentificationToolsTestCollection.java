package edu.uic.ibeis_java_api;

import edu.uic.ibeis_java_api.api.Ibeis;
import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisEncounter;
import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfosWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryHandler;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryRecordsCollectionWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryType;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.thresholds_computation.IdentificationThresholdsComputationHandler;
import edu.uic.ibeis_java_api.values.Species;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class IdentificationToolsTestCollection implements TestCollection {

    private Collection<Test> testCollection;
    private Ibeis ibeis;

    private IbeisEncounter encounter;
    private List<IbeisAnnotation> annotations = new ArrayList<>();
    private QueryRecordsCollectionWrapper queryRecordsCollectionOneVsAllWrapper;
    private QueryRecordsCollectionWrapper queryRecordsCollectionOneVsOneWrapper;
    private IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWithinDatasetThresholdsWrapper;


    public IdentificationToolsTestCollection() {
        System.out.println("***IDENTIFICATION TOOLS TEST COLLECTION***\n");
        testCollection = new ArrayList<>();
        ibeis = new Ibeis();

        try {
            encounter = ibeis.getEncounterById(4);
            for (IbeisImage image : encounter.getImages().subList(0,2)) {
                annotations.addAll(image.getAnnotations());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        testCollection.add(new QueryHandlerOneVsAllTest(new File(getClass().getClassLoader().getResource("query_resource_collection_one_vs_all_wrapper.json").getFile())));
        testCollection.add(new QueryHandlerOneVsOneTest(new File(getClass().getClassLoader().getResource("query_resource_collection_one_vs_one_wrapper.json").getFile())));
        testCollection.add(new IdentificationThresholdsWithinDatasetComputationHandlerTest());
    }


    @Override
    public void runTests() {
        for(Test t : testCollection) {
            t.execute();
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }

    public class QueryHandlerOneVsAllTest implements Test {

        private File queryRecordsCollectionWrapperFile;

        public QueryHandlerOneVsAllTest(File queryRecordsCollectionWrapperFile) {
            this.queryRecordsCollectionWrapperFile = queryRecordsCollectionWrapperFile;
        }

        @Override
        public String getTestType() {
            return null;
        }

        @Override
        public void execute() {
            try {
                queryRecordsCollectionOneVsAllWrapper = new QueryHandler(annotations,annotations, Species.GIRAFFE, null, Arrays.asList("NAG")).execute(queryRecordsCollectionWrapperFile).getQueryRecordsCollectionWrapper();
                System.out.println("\nJSON OUTPUT: " + queryRecordsCollectionOneVsAllWrapper.toJson());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class QueryHandlerOneVsOneTest implements Test {

        private File queryRecordsCollectionWrapperFile;

        public QueryHandlerOneVsOneTest(File queryRecordsCollectionWrapperFile) {
            this.queryRecordsCollectionWrapperFile = queryRecordsCollectionWrapperFile;
        }

        @Override
        public String getTestType() {
            return null;
        }

        @Override
        public void execute() {
            try {
                queryRecordsCollectionOneVsOneWrapper = new QueryHandler(annotations,annotations,Species.GIRAFFE, QueryType.ONE_VS_ONE, null, Arrays.asList("NAG")).execute(queryRecordsCollectionWrapperFile).getQueryRecordsCollectionWrapper();
                System.out.println("\nJSON OUTPUT: " + queryRecordsCollectionOneVsOneWrapper.toJson());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class IdentificationThresholdsWithinDatasetComputationHandlerTest implements Test {

        @Override
        public String getTestType() {
            return null;
        }

        @Override
        public void execute() {
            try {
                ibeisDbAnnotationInfosWithinDatasetThresholdsWrapper = new IdentificationThresholdsComputationHandler(queryRecordsCollectionOneVsAllWrapper).execute(new File("")).getResult();
                System.out.println("\nJSON OUTPUT: " + ibeisDbAnnotationInfosWithinDatasetThresholdsWrapper.toJson());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
