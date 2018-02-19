package net.rhari.ecomm.products;

import net.rhari.ecomm.data.model.Product;
import net.rhari.ecomm.data.repository.ProductRepository;
import net.rhari.ecomm.di.ActivityScoped;
import net.rhari.ecomm.util.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@ActivityScoped
class ProductsPresenter implements ProductsContract.Presenter,
        NetworkHelper.OnNetworkStateChangeListener {

    private final ProductRepository productRepository;
    private final NetworkHelper networkHelper;

    private ProductsContract.View view;

    private int categoryId;
    private final List<Product> products;
    private final CompositeDisposable disposables;

    @Inject
    ProductsPresenter(ProductRepository productRepository, NetworkHelper networkHelper) {
        this.productRepository = productRepository;
        this.networkHelper = networkHelper;

        this.products = new ArrayList<>(0);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void subscribe(ProductsContract.View view, ProductsContract.State state) {
        this.view = view;

        networkHelper.addNetworkStateChangeListener(this);
        products.clear();
        if (state != null) {
            loadState(state);
        } else {
            reload();
        }
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void onListItemClick(Product product, int position) {
        view.goToVariantsPage(product.getId());
    }

    @Override
    public void onRetry() {
        reload();
    }

    @Override
    public void onNetworkStateChange(boolean connected) {
        if (connected) {
            reload();
        }
    }

    @Override
    public ProductsContract.State getState() {
        return new ProductsState(categoryId, products, null);
    }

    @Override
    public void unsubscribe() {
        view = null;
        disposables.clear();
        networkHelper.removeNetworkStateChangeListener(this);
    }

    private void reload() {
        view.hideErrorMessage();
        if (!networkHelper.isConnected()) {
            view.showErrorMessage(networkHelper.getNetworkUnavailableString());
            view.hideLoadingContentIndicator();
            return;
        }
        view.showLoadingContentIndicator();
        Disposable disposable = productRepository.getProducts(categoryId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> view.showErrorMessage(null))
                .subscribe(this::saveAndShowProducts);
        disposables.add(disposable);
    }

    private void loadState(ProductsContract.State state) {
        categoryId = state.getCategoryId();
        saveAndShowProducts(state.getProducts());
        view.restoreListState(state.getProductsListState());
    }

    private void saveAndShowProducts(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        view.updateProductsList(products);
        view.hideLoadingContentIndicator();
    }
}