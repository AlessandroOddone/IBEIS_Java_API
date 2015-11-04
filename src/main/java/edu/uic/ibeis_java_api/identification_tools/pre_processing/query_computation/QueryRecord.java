package edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;

/**
 * Record of each query executed by a
 */
public class QueryRecord {
    private IbeisAnnotation queryAnnotation;
    private IbeisAnnotation dbAnnotation;
    private double score;
    private boolean sameIndividual;
    private boolean queryAnnotationOfTargetSpecies;
    private boolean dbAnnotationOfTargetSpecies;
    private boolean queryAnnotationOutsider;
    private boolean dbAnnotationOutsider;

    public IbeisAnnotation getQueryAnnotation() {
        return queryAnnotation;
    }

    public IbeisAnnotation getDbAnnotation() {
        return dbAnnotation;
    }

    public double getScore() {
        return score;
    }

    public boolean isSameIndividual() {
        return sameIndividual;
    }

    public boolean isQueryAnnotationOfTargetSpecies() {
        return queryAnnotationOfTargetSpecies;
    }

    public boolean isDbAnnotationOfTargetSpecies() {
        return dbAnnotationOfTargetSpecies;
    }

    public boolean isQueryAnnotationOutsider() {
        return queryAnnotationOutsider;
    }

    public boolean isDbAnnotationOutsider() {
        return dbAnnotationOutsider;
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

    public void setSameIndividual(boolean sameIndividual) {
        this.sameIndividual = sameIndividual;
    }

    public void setQueryAnnotationOfTargetSpecies(boolean queryAnnotationOfTargetSpecies) {
        this.queryAnnotationOfTargetSpecies = queryAnnotationOfTargetSpecies;
    }

    public void setDbAnnotationOfTargetSpecies(boolean dbAnnotationOfTargetSpecies) {
        this.dbAnnotationOfTargetSpecies = dbAnnotationOfTargetSpecies;
    }

    public void setQueryAnnotationOutsider(boolean queryAnnotationOutsider) {
        this.queryAnnotationOutsider = queryAnnotationOutsider;
    }

    public void setDbAnnotationOutsider(boolean dbAnnotationOutsider) {
        this.dbAnnotationOutsider = dbAnnotationOutsider;
    }

    @Override
    public String toString() {
        return "[query_aid: " + queryAnnotation.getId() + ", db_aid: " + dbAnnotation.getId() + ", score:" + score +
                ", same_individual: " + sameIndividual + ", query_annotation_of_target_species: " +
                queryAnnotationOfTargetSpecies + ", db_annotation_of_target_species: " +
                dbAnnotationOfTargetSpecies + ", query_annotation_outsider: " +
                queryAnnotationOutsider + ", db_annotation_outsider: " +
                dbAnnotationOutsider + "]";
    }
}
