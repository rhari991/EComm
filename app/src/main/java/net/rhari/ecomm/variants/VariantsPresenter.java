package net.rhari.ecomm.variants;

import net.rhari.ecomm.data.model.Variant;
import net.rhari.ecomm.data.repository.VariantRepository;
import net.rhari.ecomm.di.ActivityScoped;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@ActivityScoped
class VariantsPresenter implements VariantsContract.Presenter {

    private final VariantRepository variantRepository;

    private VariantsContract.View view;

    private int productId;
    private final List<Variant> variants;
    private final CompositeDisposable disposables;

    @Inject
    VariantsPresenter(VariantRepository variantRepository) {
        this.variantRepository = variantRepository;

        this.variants = new ArrayList<>(0);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void subscribe(VariantsContract.View view, VariantsContract.State state) {
        this.view = view;

        variants.clear();
        if (state != null) {
            loadState(state);
        } else {
            reload();
        }
    }

    @Override
    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public void onRetry() {
        reload();
    }

    @Override
    public VariantsContract.State getState() {
        return new VariantState(productId, variants, null);
    }

    @Override
    public void unsubscribe() {
        view = null;
        disposables.clear();
    }

    private void reload() {
        view.hideErrorMessage();
        view.showLoadingContentIndicator();
        Disposable disposable = variantRepository.getVariants(productId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> view.showErrorMessage())
                .subscribe(this::saveAndShowVariants);
        disposables.add(disposable);
    }

    private void loadState(VariantsContract.State state) {
        productId = state.getProductId();
        saveAndShowVariants(state.getVariants());
        view.restoreListState(state.getVariantsListState());
    }

    private void saveAndShowVariants(List<Variant> variants) {
        this.variants.clear();
        this.variants.addAll(variants);
        view.updateVariantList(variants);
        view.hideLoadingContentIndicator();
    }
}