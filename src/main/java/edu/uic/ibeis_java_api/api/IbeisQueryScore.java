package edu.uic.ibeis_java_api.api;

public class IbeisQueryScore implements Comparable<IbeisQueryScore> {

    private IbeisAnnotation dbAnnotation;
    private Double score;

    public IbeisQueryScore(IbeisAnnotation dbAnnotation, Double score) {
        this.dbAnnotation = dbAnnotation;
        this.score = score;
    }

    public IbeisAnnotation getDbAnnotation() {
        return dbAnnotation;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public int compareTo(IbeisQueryScore other) {
        return score.compareTo(other.getScore());
    }

    @Override
    public String toString() {
        return "[id: " + dbAnnotation.getId() + ", score: " + score +"]";
    }
}
