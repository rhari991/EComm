package net.rhari.ecomm.products;

import net.rhari.ecomm.data.model.RankingInfo;
import net.rhari.ecomm.data.model.SortedProduct;
import net.rhari.ecomm.data.repository.RankingRepository;
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

    private final RankingRepository rankingRepository;

    private ProductsContract.View view;

    private int categoryId;
    private RankingInfo currentSortOrder;
    private final List<RankingInfo> sortOrderOptions;
    private final List<SortedProduct> products;
    private final CompositeDisposable disposables;

    @Inject
    ProductsPresenter(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;

        this.sortOrderOptions = new ArrayList<>(0);
        this.products = new ArrayList<>(0);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void subscribe(ProductsContract.View view, ProductsContract.State state) {
        this.view = view;

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
    public void onSortOrderChange(RankingInfo newOrder) {
        currentSortOrder = newOrder;
        reloadSortedProducts();
    }

    @Override
    public void onListItemClick(SortedProduct product, int position) {
        view.goToVariantsPage(product.getProduct().getId());
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
        return new ProductsState(categoryId, currentSortOrder, sortOrderOptions, products, null);
    }

    @Override
    public void unsubscribe() {
        view = null;
        disposables.clear();
    }

    private void reload() {
        view.hideErrorMessage();
        view.showLoadingContentIndicator();
        Disposable disposable = rankingRepository.getAllRankingInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> view.showErrorMessage(null))
                .subscribe(this::processSortOrderOptions);
        disposables.add(disposable);
    }

    private void processSortOrderOptions(List<RankingInfo> sortOrderOptions) {
        saveAndShowSortOrderOptions(sortOrderOptions);
        currentSortOrder = sortOrderOptions.get(0);
        reloadSortedProducts();
    }

    private void saveAndShowSortOrderOptions(List<RankingInfo> sortOrderOptions) {
        this.sortOrderOptions.clear();
        this.sortOrderOptions.addAll(sortOrderOptions);
        view.updateSortOrderOptions(sortOrderOptions);
    }

    private void reloadSortedProducts() {
        Disposable disposable = rankingRepository.getSortedProducts(currentSortOrder.getId(),
                categoryId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> view.showErrorMessage(null))
                .subscribe(this::saveAndShowProducts);
        disposables.add(disposable);
    }

    private void loadState(ProductsContract.State state) {
        categoryId = state.getCategoryId();
        currentSortOrder = state.getCurrentSortOrder();
        saveAndShowSortOrderOptions(state.getSortOrderOptions());
        saveAndShowProducts(state.getProducts());
        view.restoreListState(state.getProductsListState());
    }

    private void saveAndShowProducts(List<SortedProduct> products) {
        this.products.clear();
        this.products.addAll(products);
        view.updateProductsList(products, currentSortOrder);
        view.hideLoadingContentIndicator();
    }
}