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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import net.rhari.ecomm.R;
import net.rhari.ecomm.data.model.RankingInfo;
import net.rhari.ecomm.data.model.SortedProduct;
import net.rhari.ecomm.di.ActivityScoped;
import net.rhari.ecomm.util.ListState;
import net.rhari.ecomm.util.RecyclerViewState;
import net.rhari.ecomm.variants.VariantsActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

@ActivityScoped
public class ProductsActivity extends DaggerAppCompatActivity implements ProductsContract.View,
        ProductsAdapter.OnListItemClickListener, Spinner.OnItemSelectedListener {

    public static final String EXTRA_CATEGORY_ID = "category_id";

    private static final String BUNDLE_ARGUMENT_CATEGORY_ID = "category_id";
    private static final String BUNDLE_ARGUMENT_CURRENT_SORT_ORDER = "current_sort_order";
    private static final String BUNDLE_ARGUMENT_SORT_ORDER_OPTIONS = "sort_order_options";
    private static final String BUNDLE_ARGUMENT_PRODUCTS = "products";
    private static final String BUNDLE_ARGUMENT_PRODUCT_LIST_STATE = "product_list_state";

    @BindView(R.id.layout_loading_content)
    LinearLayout loadingContentView;

    @BindView(R.id.recyclerview)
    RecyclerView productsListView;

    @Inject
    ProductsContract.Presenter presenter;

    private ProductsAdapter productsAdapter;
    private ArrayAdapter<RankingInfo> sortOrderAdapter;
    private Snackbar errorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_list);
        sortOrderAdapter = new ArrayAdapter<>(this, R.layout.layout_sort_order_row);
        ButterKnife.bind(this);
        setupToolbar();
        setupProductsList();
        subscribeToPresenter(null);
    }

    @Override
    protected void onStop() {
        presenter.unsubscribe();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.products_page_options, menu);
        MenuItem item = menu.findItem(R.id.sort_order);
        Spinner spinner = (Spinner) item.getActionView();
        spinner.setAdapter(sortOrderAdapter);
        spinner.setOnItemSelectedListener(this);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void updateSortOrderOptions(List<RankingInfo> sortOrderOptions) {
        sortOrderAdapter.clear();
        sortOrderAdapter.addAll(sortOrderOptions);
        sortOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateProductsList(List<SortedProduct> products, RankingInfo sortOrder) {
        productsAdapter.swapData(products, sortOrder.getMetricName());
    }

    @Override
    public void goToVariantsPage(int productId) {
        Intent intent = new Intent(this, VariantsActivity.class);
        intent.putExtra(VariantsActivity.EXTRA_PRODUCT_ID, productId);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        RankingInfo newOrder = sortOrderAdapter.getItem(position);
        presenter.onSortOrderChange(newOrder);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onListItemClick(SortedProduct product, int position) {
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
        productsAdapter = new ProductsAdapter(new ArrayList<>(0));
        productsAdapter.setOnClickListener(this);
        productsListView.setAdapter(productsAdapter);
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
        Parcelable currentSortOrderState = Parcels.wrap(state.getCurrentSortOrder());
        savedInstanceState.putParcelable(BUNDLE_ARGUMENT_CURRENT_SORT_ORDER, currentSortOrderState);
        Parcelable sortOrderOptionsState = Parcels.wrap(state.getSortOrderOptions());
        savedInstanceState.putParcelable(BUNDLE_ARGUMENT_SORT_ORDER_OPTIONS, sortOrderOptionsState);
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
            categoryId = savedInstanceState.getInt(BUNDLE_ARGUMENT_CATEGORY_ID);
        }
        RankingInfo currentSortOrder = null;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_CURRENT_SORT_ORDER)) {
            currentSortOrder = Parcels.unwrap(savedInstanceState
                    .getParcelable(BUNDLE_ARGUMENT_CURRENT_SORT_ORDER));
        }
        List<RankingInfo> sortOrderOptions = null;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_SORT_ORDER_OPTIONS)) {
            sortOrderOptions = Parcels.unwrap(savedInstanceState
                    .getParcelable(BUNDLE_ARGUMENT_SORT_ORDER_OPTIONS));
        }
        List<SortedProduct> products = null;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_PRODUCTS)) {
            products = Parcels.unwrap(savedInstanceState.getParcelable(BUNDLE_ARGUMENT_PRODUCTS));
        }
        Parcelable productsListState = null;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_PRODUCT_LIST_STATE)) {
            productsListState = savedInstanceState.getParcelable(BUNDLE_ARGUMENT_PRODUCT_LIST_STATE);
        }
        return new ProductsState(categoryId, currentSortOrder, sortOrderOptions, products, new
                RecyclerViewState
                (productsListState));
    }
}