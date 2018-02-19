package net.rhari.ecomm.products;

import net.rhari.ecomm.data.model.Product;
import net.rhari.ecomm.util.ListState;

import java.util.List;

class ProductsState implements ProductsContract.State {

    private final int categoryId;
    private final List<Product> products;
    private final ListState productsListState;

    ProductsState(int categoryId, List<Product> products, ListState productsListState) {
        this.categoryId = categoryId;
        this.products = products;
        this.productsListState = productsListState;
    }

    @Override
    public int getCategoryId() {
        return categoryId;
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public ListState getProductsListState() {
        return productsListState;
    }
}