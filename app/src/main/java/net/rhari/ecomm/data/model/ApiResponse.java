package net.rhari.ecomm.data.model;

import java.util.List;

public class ApiResponse {

    private final List<Category> categories;

    private final List<Product> products;

    private final List<Variant> variants;

    private final List<RankingInfo> rankingInfo;

    private final List<RankingValue> rankingValues;

    public ApiResponse(List<Category> categories,
                       List<Product> products,
                       List<Variant> variants,
                       List<RankingInfo> rankingInfo,
                       List<RankingValue> rankingValues) {
        this.categories = categories;
        this.products = products;
        this.variants = variants;
        this.rankingInfo = rankingInfo;
        this.rankingValues = rankingValues;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public List<RankingInfo> getRankingInfo() {
        return rankingInfo;
    }

    public List<RankingValue> getRankingValues() {
        return rankingValues;
    }
}
