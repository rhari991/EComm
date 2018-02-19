package net.rhari.ecomm.products;

import net.rhari.ecomm.data.model.RankingInfo;
import net.rhari.ecomm.data.model.SortedProduct;
import net.rhari.ecomm.util.ListState;

import java.util.List;

class ProductsState implements ProductsContract.State {

    private final int categoryId;
    private RankingInfo currentSortOrder;
    private final List<RankingInfo> sortOrderOptions;
    private final List<SortedProduct> products;
    private final ListState productsListState;

    ProductsState(int categoryId, RankingInfo currentSortOrder,
                  List<RankingInfo> sortOrderOptions, List<SortedProduct> products,
                  ListState productsListState) {
        this.categoryId = categoryId;
        this.currentSortOrder = currentSortOrder;
        this.sortOrderOptions = sortOrderOptions;
        this.products = products;
        this.productsListState = productsListState;
    }

    @Override
    public int getCategoryId() {
        return categoryId;
    }

    public RankingInfo getCurrentSortOrder() {
        return currentSortOrder;
    }

    public List<RankingInfo> getSortOrderOptions() {
        return sortOrderOptions;
    }

    @Override
    public List<SortedProduct> getProducts() {
        return products;
    }

    @Override
    public ListState getProductsListState() {
        return productsListState;
    }
}