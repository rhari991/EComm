package net.rhari.ecomm.products;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import net.rhari.ecomm.R;
import net.rhari.ecomm.data.model.Product;
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
public class ProductsActivity extends DaggerAppCompatActivity implements ProductsContract.View,
        ProductsAdapter.OnListItemClickListener {

    public static final String EXTRA_CATEGORY_ID = "category_id";

    private static final String BUNDLE_ARGUMENT_CATEGORY_ID = "category_id";
    private static final String BUNDLE_ARGUMENT_PRODUCTS = "products";
    private static final String BUNDLE_ARGUMENT_PRODUCT_LIST_STATE = "product_list_state";

    @BindView(R.id.layout_loading_content)
    LinearLayout loadingContentView;

    @BindView(R.id.list_categories)
    RecyclerView productsListView;

    @Inject
    ProductsContract.Presenter presenter;

    private ProductsAdapter adapter;
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
        setupProductsList();
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
    public void updateProductsList(List<Product> products) {
        adapter.swapData(products);
    }

    @Override
    public void goToVariantsPage(int productId) {
        // TODO
    }

    @Override
    public void onListItemClick(Product product, int position) {
        presenter.onListItemClick(product, position);
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
            productsListView.getLayoutManager().onRestoreInstanceState(parcelableState);
        }
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        if (errorMessage == null) {
            errorMessage = getString(R.string.error_generic);
        }
        errorView = Snackbar.make(productsListView.getRootView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
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

    private void setupProductsList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        productsListView.setLayoutManager(layoutManager);
        productsListView.setHasFixedSize(true);
        adapter = new ProductsAdapter(new ArrayList<>(0));
        adapter.setOnClickListener(this);
        productsListView.setAdapter(adapter);
    }

    private void subscribeToPresenter(Bundle savedInstanceState) {
        if (presenter != null) {
            presenter.unsubscribe();
        }
        ProductsContract.State state = readStateFromBundle(savedInstanceState);
        if (state == null && getIntent().hasExtra(EXTRA_CATEGORY_ID)) {
            int categoryId = getIntent().getIntExtra(EXTRA_CATEGORY_ID, -1);
            presenter.setCategoryId(categoryId);
        }
        presenter.subscribe(this, state);
    }

    private void writeStateToBundle(Bundle savedInstanceState) {
        ProductsContract.State state = presenter.getState();
        savedInstanceState.putInt(BUNDLE_ARGUMENT_CATEGORY_ID, state.getCategoryId());
        Parcelable productsState = Parcels.wrap(state.getProducts());
        savedInstanceState.putParcelable(BUNDLE_ARGUMENT_PRODUCTS, productsState);
        Parcelable productsListState = productsListView.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(BUNDLE_ARGUMENT_PRODUCT_LIST_STATE, productsListState);
    }

    private ProductsContract.State readStateFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return null;
        }

        int categoryId = -1;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_CATEGORY_ID)) {
            categoryId =
                    Parcels.unwrap(savedInstanceState.getParcelable(BUNDLE_ARGUMENT_CATEGORY_ID));
        }
        List<Product> products = null;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_PRODUCTS)) {
            products = Parcels.unwrap(savedInstanceState.getParcelable(BUNDLE_ARGUMENT_PRODUCTS));
        }
        Parcelable productsListState = null;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_PRODUCT_LIST_STATE)) {
            productsListState = savedInstanceState.getParcelable(BUNDLE_ARGUMENT_PRODUCT_LIST_STATE);
        }
        return new ProductsState(categoryId, products, new RecyclerViewState(productsListState));
    }
}