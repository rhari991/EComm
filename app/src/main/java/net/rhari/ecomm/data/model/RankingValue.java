package net.rhari.ecomm.data.model;

public class RankingValue {

    private final int rankingId;

    private final int productId;

    private final int value;

    public RankingValue(int rankingId, int productId, int value) {
        this.rankingId = rankingId;
        this.productId = productId;
        this.value = value;
    }

    public int getRankingId() {
        return rankingId;
    }

    public int getProductId() {
        return productId;
    }

    public int getValue() {
        return value;
    }
}
