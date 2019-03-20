package se.soprasteria.automatedtesting.webdriver.api.utility;

public class ImageComparisonData {

    private float similarityPercentage;
    private float differencePercentage;
    private float similarityPercentage_Red;
    private float differencePercentage_Red;
    private float differencePercentage_Green;
    private float similarityPercentage_Green;
    private float similarityPercentage_Blue;
    private float differencePercentage_Blue;
    private int numberOfDecimals;

    public ImageComparisonData(float similarityPercentage, float similarityPercentage_Red, float similarityPercentage_Green, float similarityPercentage_Blue, int numberOfDecimals) {
        this.similarityPercentage = similarityPercentage;
        this.similarityPercentage_Red = similarityPercentage_Red;
        this.similarityPercentage_Green = similarityPercentage_Green;
        this.similarityPercentage_Blue = similarityPercentage_Blue;
        this.numberOfDecimals = numberOfDecimals;
    }

    public double getRoundedPercentage(double percentage) {
        return Math.round(percentage * Math.pow(10, numberOfDecimals)) / Math.pow(10, numberOfDecimals);
    }

    public double getSimilarityPercentage() {
        return getRoundedPercentage(similarityPercentage);
    }

    public double getDifferencePercentage() {
        return getRoundedPercentage(differencePercentage);
    }

    public double getDifferencePercentage_Red() {
        return getRoundedPercentage(100 - similarityPercentage_Red);
    }

    public double getSimilarityPercentage_Red() {
        return getRoundedPercentage(similarityPercentage_Red);
    }

    public double getDifferencePercentage_Green() {
        return getRoundedPercentage(100 - similarityPercentage_Green);
    }

    public double getSimilarityPercentage_Green() {
        return getRoundedPercentage(similarityPercentage_Green);
    }

    public double getDifferencePercentage_Blue() {
        return getRoundedPercentage(100 - similarityPercentage_Blue);
    }

    public double getSimilarityPercentage_Blue() {
        return getRoundedPercentage(similarityPercentage_Blue);
    }

}
