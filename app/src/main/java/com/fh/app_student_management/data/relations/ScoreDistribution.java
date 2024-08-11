package com.fh.app_student_management.data.relations;

public class ScoreDistribution {

    private int excellent;
    private int good;
    private int fair;
    private int average;

    public ScoreDistribution() {
    }

    public ScoreDistribution(int excellent, int good, int fair, int average) {
        this.excellent = excellent;
        this.good = good;
        this.fair = fair;
        this.average = average;
    }

    public int getExcellent() {
        return excellent;
    }

    public void setExcellent(int excellent) {
        this.excellent = excellent;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getFair() {
        return fair;
    }

    public void setFair(int fair) {
        this.fair = fair;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }
}
