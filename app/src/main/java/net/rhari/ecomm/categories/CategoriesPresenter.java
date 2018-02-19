package net.rhari.ecomm.categories;

import net.rhari.ecomm.data.model.Category;
import net.rhari.ecomm.data.repository.CategoryRepository;
import net.rhari.ecomm.data.repository.ProductRepository;
import net.rhari.ecomm.di.ActivityScoped;
import net.rhari.ecomm.util.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@ActivityScoped
class CategoriesPresenter implements CategoriesContract.Presenter,
        NetworkHelper.OnNetworkStateChangeListener {

    private static final int NO_PARENT_CATEGORY = -1;

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final NetworkHelper networkHelper;

    private CategoriesContract.View view;

    private int parentCategoryId;
    private final List<Category> categories;
    private final CompositeDisposable disposables;

    @Inject
    CategoriesPresenter(CategoryRepository categoryRepository, ProductRepository productRepository,
                        NetworkHelper networkHelper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.networkHelper = networkHelper;

        this.parentCategoryId = NO_PARENT_CATEGORY;
        this.categories = new ArrayList<>(0);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void subscribe(CategoriesContract.View view, CategoriesContract.State state) {
        this.view = view;

        networkHelper.addNetworkStateChangeListener(this);
        categories.clear();
        if (state != null) {
            loadState(state);
        } else {
            reload();
        }
    }

    @Override
    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    @Override
    public void onListItemClick(Category category, int position) {
        view.goToChildCategoryPage(category.getId());
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
    public CategoriesContract.State getState() {
        return new CategoriesState(parentCategoryId, categories, null);
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
        Flowable<List<Category>> source = (parentCategoryId != NO_PARENT_CATEGORY) ?
                categoryRepository.getChildCategories(parentCategoryId) :
                categoryRepository.getTopLevelCategories();
        Disposable disposable = source
                .doOnNext(categories -> {
                    if (categories.size() == 0) {
                        view.goToProductsPage(parentCategoryId);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> view.showErrorMessage(null))
                .subscribe(this::saveAndShowCategories);
        disposables.add(disposable);
    }

    private void loadState(CategoriesContract.State state) {
        parentCategoryId = state.getParentCategoryId();
        saveAndShowCategories(state.getCategories());
        view.restoreListState(state.getCategoriesListState());
    }

    private void saveAndShowCategories(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        view.updateCategoryList(categories);
        view.hideLoadingContentIndicator();
    }
}
