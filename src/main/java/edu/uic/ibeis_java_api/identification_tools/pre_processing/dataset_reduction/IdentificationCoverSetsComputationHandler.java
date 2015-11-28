package edu.uic.ibeis_java_api.identification_tools.pre_processing.dataset_reduction;

import edu.uic.ibeis_java_api.exceptions.HandlerNotExecutedException;
import edu.uic.ibeis_java_api.exceptions.InvalidThresholdTypeException;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfo;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfosWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryRecord;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryRecordsCollectionWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.thresholds_computation.ThresholdType;

import java.io.*;

public class IdentificationCoverSetsComputationHandler {

    private QueryRecordsCollectionWrapper queryRecordsCollectionWrapper;
    private IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper;
    private IdentificationCoverSetsCollectionWrapper coverSetsCollectionWrapper;

    public IdentificationCoverSetsComputationHandler(QueryRecordsCollectionWrapper queryRecordsCollectionWrapper, IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper) throws InvalidThresholdTypeException {
        if (ibeisDbAnnotationInfosWrapper.getThresholdType() != ThresholdType.ONE_VS_ONE) throw new InvalidThresholdTypeException();
        this.queryRecordsCollectionWrapper = queryRecordsCollectionWrapper;
        this.ibeisDbAnnotationInfosWrapper = ibeisDbAnnotationInfosWrapper;
        this.coverSetsCollectionWrapper = new IdentificationCoverSetsCollectionWrapper(ibeisDbAnnotationInfosWrapper.getTargetSpecies());
    }

    public IdentificationCoverSetsComputationHandler execute(File coverSetsCollectionWrapperFile) {
        IdentificationCoverSetsCollectionWrapper temp = readCoverSetsCollectionWrapperFromFile(coverSetsCollectionWrapperFile);
        if (temp != null) {
            coverSetsCollectionWrapper = temp;
        }

        int ANNOT_INFO_INDEX = 0;
        int NUM_ANNOT_INFOS = ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationInfosMap().size();

        for(IbeisDbAnnotationInfo ibeisDbAnnotationInfo : ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationInfosMap().values()) {
            ANNOT_INFO_INDEX++;

            if (!ibeisDbAnnotationInfo.isOutsider() && !coverSetAlreadyComputed(ibeisDbAnnotationInfo)) {
                IdentificationCoverSet coverSet = new IdentificationCoverSet(ibeisDbAnnotationInfo);

                int QUERY_RECORD_INDEX = 0;
                int NUM_QUERY_RECORDS = queryRecordsCollectionWrapper.getRecords().size();

                for(QueryRecord queryRecord : queryRecordsCollectionWrapper.getRecords()) {
                    System.out.println("ANNOT INFO " + ANNOT_INFO_INDEX + "/" + NUM_ANNOT_INFOS + ": " + "query record " + QUERY_RECORD_INDEX++ + "/" + NUM_QUERY_RECORDS);
                    if(queryRecord.getDbAnnotation().getId() == ibeisDbAnnotationInfo.getAnnotation().getId()) {
                        if(queryRecord.getScore() >= ibeisDbAnnotationInfo.getRecognitionThreshold() && queryRecord.isSameIndividual()) {
                            coverSet.add(queryRecord.getQueryAnnotation());
                        }
                    }
                }
                coverSetsCollectionWrapper.add(coverSet);
                writeCoverSetsCollectionWrapperToFile(coverSetsCollectionWrapperFile);
            }
        }
        return this;
    }

    public IdentificationCoverSetsCollectionWrapper getResult() throws HandlerNotExecutedException{
        if (coverSetsCollectionWrapper == null) throw new HandlerNotExecutedException();
        return coverSetsCollectionWrapper;
    }

    private boolean coverSetAlreadyComputed(IbeisDbAnnotationInfo ibeisDbAnnotationInfo) {
        for (IdentificationCoverSet identificationCoverSet : coverSetsCollectionWrapper.getCoverSets()) {
            if (identificationCoverSet.getDbAnnotationInfo().getAnnotation().getId() == ibeisDbAnnotationInfo.getAnnotation().getId()) {
                return true;
            }
        }
        return false;
    }

    private void writeCoverSetsCollectionWrapperToFile(File file) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(coverSetsCollectionWrapper.toJson());
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

    private IdentificationCoverSetsCollectionWrapper readCoverSetsCollectionWrapperFromFile(File file) {
        BufferedReader reader = null;
        IdentificationCoverSetsCollectionWrapper coverSetsCollectionWrapper = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            coverSetsCollectionWrapper = IdentificationCoverSetsCollectionWrapper.fromJson(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return coverSetsCollectionWrapper;
    }

}
