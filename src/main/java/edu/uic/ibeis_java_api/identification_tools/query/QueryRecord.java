package edu.uic.ibeis_java_api.identification_tools.query;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;

public class QueryRecord {
    private IbeisAnnotation queryAnnotation;
    private IbeisAnnotation dbAnnotation;
    private double score;
    private boolean giraffe;
    private boolean sameGiraffe;

    public IbeisAnnotation getQueryAnnotation() {
        return queryAnnotation;
    }

    public IbeisAnnotation getDbAnnotation() {
        return dbAnnotation;
    }

    public double getScore() {
        return score;
    }

    public boolean isGiraffe() {
        return giraffe;
    }

    public boolean isSameGiraffe() {
        return sameGiraffe;
    }

    public void setQueryAnnotation(IbeisAnnotation queryAnnotation) {
        this.queryAnnotation = queryAnnotation;
    }

    public void setDbAnnotation(IbeisAnnotation dbAnnotation) {
        this.dbAnnotation = dbAnnotation;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setGiraffe(boolean giraffe) {
        this.giraffe = giraffe;
    }

    public void setSameGiraffe(boolean sameGiraffe) {
        this.sameGiraffe = sameGiraffe;
    }

    @Override
    public String toString() {
        return "[query_aid: " + queryAnnotation.getId() + ", db_aid: " + dbAnnotation.getId() + ", score:" + score +
                ", giraffe: " + giraffe + ", sameGiraffe: " + sameGiraffe + "]";
    }
}
