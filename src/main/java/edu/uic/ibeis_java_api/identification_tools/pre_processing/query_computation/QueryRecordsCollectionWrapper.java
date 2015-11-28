package edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.uic.ibeis_java_api.interfaces.JsonSerializable;
import edu.uic.ibeis_java_api.values.Species;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for a collection of QueryRecord objects. Includes attributes with common values for all the objects in the collection.
 */
public class QueryRecordsCollectionWrapper implements JsonSerializable<QueryRecordsCollectionWrapper> {

    private QueryType queryType;
    private Species targetSpecies;
    private List<QueryRecord> records;

    public QueryRecordsCollectionWrapper(QueryType queryType, Species targetSpecies) {
        this.queryType = queryType;
        this.targetSpecies = targetSpecies;
        records = new ArrayList<>();
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public Species getTargetSpecies() {
        return targetSpecies;
    }

    public List<QueryRecord> getRecords() {
        return records;
    }

    public void add(QueryRecord queryRecord) {
        records.add(queryRecord);
    }

    @Override
    public String toJson() {
        return getGson().toJson(this);
    }

    public static QueryRecordsCollectionWrapper fromJson(String jsonString) {
        return getGson().fromJson(jsonString, QueryRecordsCollectionWrapper.class);
    }

    private static Gson getGson() {
        return new GsonBuilder().serializeNulls().create();
    }
}
