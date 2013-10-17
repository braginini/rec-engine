package com.zibea.recommendations.common.model;

/**
 * @author Mikhail Bragin
 */
public class Similarity {

    private long itemA;

    private long itemB;

    private double similarity;

    public Similarity(long itemA, long itemB, double similarity) {
        this.itemA = itemA;
        this.itemB = itemB;
        this.similarity = similarity;
    }

    public long getItemA() {
        return itemA;
    }

    public void setItemA(long itemA) {
        this.itemA = itemA;
    }

    public long getItemB() {
        return itemB;
    }

    public void setItemB(long itemB) {
        this.itemB = itemB;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Similarity");
        sb.append("{itemA=").append(itemA);
        sb.append(", itemB=").append(itemB);
        sb.append(", similarity=").append(similarity);
        sb.append('}');
        return sb.toString();
    }

    public enum RecommendationType {
        VIEW
    }
}
