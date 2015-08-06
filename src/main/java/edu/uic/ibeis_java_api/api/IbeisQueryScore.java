package edu.uic.ibeis_java_api.api;

public class IbeisQueryScore implements Comparable<IbeisQueryScore> {

    public final static double NULL_SCORE = -1;

    private IbeisAnnotation dbAnnotation;
    private double score;

    public IbeisQueryScore(IbeisAnnotation dbAnnotation, double score) {
        this.dbAnnotation = dbAnnotation;
        this.score = score;
    }

    public IbeisAnnotation getDbAnnotation() {
        return dbAnnotation;
    }

    public double getScore() {
        return score;
    }

    @Override
    public int compareTo(IbeisQueryScore other) {
        return Double.valueOf(score).compareTo(other.getScore());
    }

    @Override
    public String toString() {
        return "[id: " + dbAnnotation.getId() + ", score: " + score +"]";
    }
}
