package net.rhari.ecomm.variants;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import net.rhari.ecomm.R;
import net.rhari.ecomm.data.model.Variant;
import net.rhari.ecomm.di.ActivityScoped;
import net.rhari.ecomm.util.ListState;
import net.rhari.ecomm.util.RecyclerViewState;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

@ActivityScoped
public class VariantsActivity extends DaggerAppCompatActivity implements VariantsContract.View {

    public static final String EXTRA_PRODUCT_ID = "product_id";

    private static final String BUNDLE_ARGUMENT_PRODUCT_ID = "product_id";
    private static final String BUNDLE_ARGUMENT_VARIANTS = "variants";
    private static final String BUNDLE_ARGUMENT_VARIANT_LIST_STATE = "variant_list_state";

    @BindView(R.id.layout_loading_content)
    LinearLayout loadingContentView;

    @BindView(R.id.recyclerview)
    RecyclerView variantsListView;

    @Inject
    VariantsContract.Presenter presenter;

    private VariantsAdapter adapter;
    private Snackbar errorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ButterKnife.bind(this);
        setupToolbar();
        setupVariantList();
        subscribeToPresenter(null);
    }

    @Override
    protected void onPause() {
        presenter.unsubscribe();
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void updateVariantList(List<Variant> variants) {
        adapter.swapData(variants);
    }

    @Override
    public void showLoadingContentIndicator() {
        loadingContentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingContentIndicator() {
        loadingContentView.setVisibility(View.GONE);
    }

    @Override
    public void restoreListState(ListState state) {
        if (state instanceof RecyclerViewState) {
            Parcelable parcelableState = ((RecyclerViewState) state).getListState();
            variantsListView.getLayoutManager().onRestoreInstanceState(parcelableState);
        }
    }

    @Override
    public void showErrorMessage() {
        String errorMessage = getString(R.string.error_generic);
        errorView = Snackbar.make(variantsListView.getRootView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.action_retry), view -> presenter.onRetry());
        errorView.show();
    }

    @Override
    public void hideErrorMessage() {
        if (errorView != null && errorView.isShown()) {
            errorView.dismiss();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        writeStateToBundle(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        subscribeToPresenter(savedInstanceState);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    private void setupVariantList() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        variantsListView.setLayoutManager(layoutManager);
        variantsListView.setHasFixedSize(true);
        adapter = new VariantsAdapter(new ArrayList<>(0));
        variantsListView.setAdapter(adapter);
    }

    private void subscribeToPresenter(Bundle savedInstanceState) {
        if (presenter != null) {
            presenter.unsubscribe();
        }
        VariantsContract.State state = readStateFromBundle(savedInstanceState);
        if (state == null && getIntent().hasExtra(EXTRA_PRODUCT_ID)) {
            int productId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
            presenter.setProductId(productId);
        }
        presenter.subscribe(this, state);
    }

    private void writeStateToBundle(Bundle savedInstanceState) {
        VariantsContract.State state = presenter.getState();
        savedInstanceState.putInt(BUNDLE_ARGUMENT_PRODUCT_ID, state.getProductId());
        Parcelable variantsState = Parcels.wrap(state.getVariants());
        savedInstanceState.putParcelable(BUNDLE_ARGUMENT_VARIANTS, variantsState);
        Parcelable variantsListState = variantsListView.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(BUNDLE_ARGUMENT_VARIANT_LIST_STATE, variantsListState);
    }

    private VariantsContract.State readStateFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return null;
        }

        int productId = -1;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_PRODUCT_ID)) {
            productId =
                    Parcels.unwrap(savedInstanceState.getParcelable(BUNDLE_ARGUMENT_PRODUCT_ID));
        }
        List<Variant> variants = null;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_VARIANTS)) {
            variants = Parcels.unwrap(savedInstanceState.getParcelable(BUNDLE_ARGUMENT_VARIANTS));
        }
        Parcelable variantsListState = null;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_VARIANT_LIST_STATE)) {
            variantsListState =
                    savedInstanceState.getParcelable(BUNDLE_ARGUMENT_VARIANT_LIST_STATE);
        }
        return new VariantState(productId, variants, new RecyclerViewState(variantsListState));
    }
}