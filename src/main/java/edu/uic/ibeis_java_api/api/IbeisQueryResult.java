package edu.uic.ibeis_java_api.api;

import java.util.List;

public class IbeisQueryResult {

    private IbeisAnnotation queryAnnotation;
    private List<IbeisQueryScore> scores;

    public IbeisQueryResult(IbeisAnnotation queryAnnotation, List<IbeisQueryScore> scores) {
        this.scores = scores;
        this.queryAnnotation = queryAnnotation;
    }

    public IbeisAnnotation getQueryAnnotation() {
        return queryAnnotation;
    }

    public List<IbeisQueryScore> getScores() {
        return scores;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[id: " + queryAnnotation.getId() + ", scores: {");
        for(IbeisQueryScore score : scores) {
            builder.append(score + ", ");
        }
        builder.delete(builder.lastIndexOf(","), builder.length());
        builder.append("}]");

        return builder.toString();
    }
}
