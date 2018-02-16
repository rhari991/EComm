package net.rhari.ecomm.data.model;

public class RankingInfo {

    private final int id;

    private final String description;

    private final String metricName;

    public RankingInfo(int id, String description, String metricName) {
        this.id = id;
        this.description = description;
        this.metricName = metricName;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getMetricName() {
        return metricName;
    }
}
