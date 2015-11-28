package edu.uic.ibeis_java_api.identification_tools.pre_processing.thresholds_computation;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.exceptions.HandlerNotExecutedException;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfo;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfosWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryRecord;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryRecordsCollectionWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryType;

import java.io.*;
import java.util.*;

/**
 * Handler class to calculate thresholds for annotations to be considered as database annotations, given a collection of query results
 */
public class IdentificationThresholdsComputationHandler {

    private QueryRecordsCollectionWrapper queryRecordsCollectionWrapper;
    private IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper;

    private int currentDbAnnotationIndex;

    public IdentificationThresholdsComputationHandler(QueryRecordsCollectionWrapper queryRecordsCollectionWrapper) {
        this.queryRecordsCollectionWrapper = queryRecordsCollectionWrapper;
        ThresholdType thresholdType = queryRecordsCollectionWrapper.getQueryType() == QueryType.ONE_VS_ONE ?
                ThresholdType.ONE_VS_ONE : ThresholdType.WITHIN_DATASET;
        this.ibeisDbAnnotationInfosWrapper = new IbeisDbAnnotationInfosWrapper(thresholdType, queryRecordsCollectionWrapper.getTargetSpecies());
    }

    public IdentificationThresholdsComputationHandler execute(File ibeisDbAnnotationInfosWrapperFile) throws MalformedHttpRequestException, UnsuccessfulHttpRequestException, IOException {
        return execute(50, 0.001, 0.95, 0.99, ibeisDbAnnotationInfosWrapperFile);
    }

    public IdentificationThresholdsComputationHandler execute(int maxThreshold, double step, double targetIndividualIdentificationPrecision, double targetSpeciesIdentificationPrecision, File ibeisDbAnnotationInfosWrapperFile) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException {
        List<IbeisAnnotation> dbAnnotations = new ArrayList<>();
        Map<IbeisAnnotation, List<QueryRecord>> dbAnnotationsQueryRecordsMap = new HashMap<>();

        int RECORD_INDEX = 0;
        int NUM_RECORDS = queryRecordsCollectionWrapper.getRecords().size();

        for (QueryRecord queryRecord : queryRecordsCollectionWrapper.getRecords()) {
            if (!queryRecord.isDbAnnotationOutsider()) {
                if (!dbAnnotations.contains(queryRecord.getDbAnnotation())) {
                    IbeisAnnotation annotation = queryRecord.getDbAnnotation();
                    List<QueryRecord> recordsList = new ArrayList<>();
                    recordsList.add(queryRecord);
                    dbAnnotationsQueryRecordsMap.put(annotation,recordsList);
                    dbAnnotations.add(annotation);
                } else {
                    dbAnnotationsQueryRecordsMap.get(queryRecord.getDbAnnotation()).add(queryRecord);
                }
            } else {
                IbeisDbAnnotationInfo ibeisDbAnnotationInfo = new IbeisDbAnnotationInfo(queryRecord.getDbAnnotation());
                ibeisDbAnnotationInfo.setOfTargetSpecies(queryRecord.isDbAnnotationOfTargetSpecies());
                ibeisDbAnnotationInfo.setOutsider(true);
                ibeisDbAnnotationInfosWrapper.add(ibeisDbAnnotationInfo);
            }
            System.out.println("QUERY RECORD " + RECORD_INDEX++ +"/"+ NUM_RECORDS);
        }

        IbeisDbAnnotationInfosWrapper temp = readIbeisDbAnnotationInfosWrapperFromFile(ibeisDbAnnotationInfosWrapperFile);
        if (temp != null && dbAnnotationsQueryRecordsMap.get(temp.getIbeisDbAnnotationList().get(temp.getIbeisDbAnnotationList().size()-1)) != null) {
            ibeisDbAnnotationInfosWrapper = temp;
            currentDbAnnotationIndex =  dbAnnotations.indexOf(ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationList()
                    .get(ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationList().size() - 1)) + 1;
        } else {
            currentDbAnnotationIndex = 0;
        }

        int DB_ANNOT_INDEX = currentDbAnnotationIndex;
        int NUM_DB_ANNOTS = dbAnnotations.size();

        IbeisAnnotation dbAnnotation;
        for (int j=currentDbAnnotationIndex; j<dbAnnotations.size(); j++) {
            DB_ANNOT_INDEX++;

            dbAnnotation = dbAnnotations.get(j);

            double recognitionThreshold = Double.POSITIVE_INFINITY;
            double isOfTargetSpeciesThreshold = Double.POSITIVE_INFINITY;
            for (int i=0; i <= maxThreshold / step; i++) {
                double threshold = i * step;
                double recTP = 0;
                double recFP = 0;
                double isGiraffeTP = 0;
                double isGiraffeFP = 0;

                System.out.println("DB ANNOTATION " + DB_ANNOT_INDEX + "/" + NUM_DB_ANNOTS + " (threshold: " + threshold + ")");

                for (QueryRecord queryRecord : dbAnnotationsQueryRecordsMap.get(dbAnnotation)) {
                    //recognition threshold
                    if (queryRecord.getScore() >= threshold) {//positive
                        if (queryRecord.isSameIndividual()) {//true
                            recTP++;
                        } else {//false
                            recFP++;
                        }
                    }
                    //is-a-giraffe threshold
                    if (queryRecord.getScore() >= threshold) {//recognised as giraffe
                        if (queryRecord.isQueryAnnotationOfTargetSpecies()) {//true positive
                            isGiraffeTP++;
                        } else {//false positive
                            isGiraffeFP++;
                        }
                    }
                }
                double recognitionPrec = (recTP / (recTP + recFP));
                double isGiraffePrec = (isGiraffeTP / (isGiraffeTP + isGiraffeFP));

                if (recognitionThreshold == Double.POSITIVE_INFINITY) {//recognition threshold not found yet
                    if (recognitionPrec >= targetIndividualIdentificationPrecision) {
                        recognitionThreshold = threshold;
                    }
                }
                if (isOfTargetSpeciesThreshold == Double.POSITIVE_INFINITY) {//is-a-giraffe threshold not found yet
                    if (isGiraffePrec >= targetSpeciesIdentificationPrecision) {
                        isOfTargetSpeciesThreshold = threshold;
                    }
                }
            }
            IbeisDbAnnotationInfo ibeisDbAnnotationInfo = new IbeisDbAnnotationInfo(dbAnnotation);
            ibeisDbAnnotationInfo.setIsOfTargetSpeciesThreshold(isOfTargetSpeciesThreshold);
            ibeisDbAnnotationInfo.setRecognitionThreshold(recognitionThreshold);
            ibeisDbAnnotationInfo.setOfTargetSpecies(true);
            ibeisDbAnnotationInfo.setOutsider(false);
            ibeisDbAnnotationInfosWrapper.add(ibeisDbAnnotationInfo);
            writeIbeisDbAnnotationInfosToFile(ibeisDbAnnotationInfosWrapperFile);
        }
        return this;
    }

    public IbeisDbAnnotationInfosWrapper getResult() throws HandlerNotExecutedException {
        if (ibeisDbAnnotationInfosWrapper == null) throw new HandlerNotExecutedException();
        return ibeisDbAnnotationInfosWrapper;
    }

    private void writeIbeisDbAnnotationInfosToFile(File file) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(ibeisDbAnnotationInfosWrapper.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private IbeisDbAnnotationInfosWrapper readIbeisDbAnnotationInfosWrapperFromFile(File file) {
        BufferedReader reader = null;
        IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            ibeisDbAnnotationInfosWrapper = IbeisDbAnnotationInfosWrapper.fromJson(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ibeisDbAnnotationInfosWrapper;
    }
}
