package edu.uic.ibeis_java_api.identification_tools.query;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.uic.ibeis_java_api.interfaces.JsonSerializable;

import java.util.ArrayList;
import java.util.Collection;

public class QueryRecordsCollectionWrapper implements JsonSerializable<QueryRecordsCollectionWrapper> {

    private QueryType queryType;
    private Collection<QueryRecord> records;

    public QueryRecordsCollectionWrapper(QueryType queryType) {
        this.queryType = queryType;
        records = new ArrayList<>();
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public Collection<QueryRecord> getRecords() {
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
