package net.rhari.ecomm.categories;

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
import net.rhari.ecomm.data.model.Category;
import net.rhari.ecomm.di.ActivityScoped;
import net.rhari.ecomm.products.ProductsActivity;
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
public class CategoriesActivity extends DaggerAppCompatActivity implements CategoriesContract.View,
        CategoriesAdapter.OnListItemClickListener {

    private static final String EXTRA_PARENT_CATEGORY_ID = "parent_category_id";

    private static final String BUNDLE_ARGUMENT_PARENT_CATEGORY_ID = "parent_category_id";
    private static final String BUNDLE_ARGUMENT_CATEGORIES = "categories";
    private static final String BUNDLE_ARGUMENT_CATEGORY_LIST_STATE = "category_list_state";

    @BindView(R.id.layout_loading_content)
    LinearLayout loadingContentView;

    @BindView(R.id.recyclerview)
    RecyclerView categoriesListView;

    @Inject
    CategoriesContract.Presenter presenter;

    private CategoriesAdapter adapter;
    private Snackbar errorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_list);
        ButterKnife.bind(this);
        setupToolbar();
        setupCategoryList();
        subscribeToPresenter(null);
    }

    @Override
    protected void onStop() {
        presenter.unsubscribe();
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void updateCategoryList(List<Category> categories) {
        adapter.swapData(categories);
    }

    @Override
    public void goToChildCategoryPage(int categoryId) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putExtra(EXTRA_PARENT_CATEGORY_ID, categoryId);
        startActivity(intent);
    }

    @Override
    public void goToProductsPage(int categoryId) {
        Intent intent = new Intent(this, ProductsActivity.class);
        intent.putExtra(ProductsActivity.EXTRA_CATEGORY_ID, categoryId);
        startActivity(intent);
    }

    @Override
    public void onListItemClick(Category category, int position) {
        presenter.onListItemClick(category, position);
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
            categoriesListView.getLayoutManager().onRestoreInstanceState(parcelableState);
        }
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        if (errorMessage == null) {
            errorMessage = getString(R.string.error_generic);
        }
        errorView = Snackbar.make(categoriesListView.getRootView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
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

    private void setupCategoryList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        categoriesListView.setLayoutManager(layoutManager);
        categoriesListView.setHasFixedSize(true);
        adapter = new CategoriesAdapter(new ArrayList<>(0));
        adapter.setOnClickListener(this);
        categoriesListView.setAdapter(adapter);
    }

    private void subscribeToPresenter(Bundle savedInstanceState) {
        if (presenter != null) {
            presenter.unsubscribe();
        }
        CategoriesContract.State state = readStateFromBundle(savedInstanceState);
        if (state == null && getIntent().hasExtra(EXTRA_PARENT_CATEGORY_ID)) {
            int parentCategoryId = getIntent().getIntExtra(EXTRA_PARENT_CATEGORY_ID, -1);
            presenter.setParentCategoryId(parentCategoryId);
        }
        presenter.subscribe(this, state);
    }

    private void writeStateToBundle(Bundle savedInstanceState) {
        CategoriesContract.State state = presenter.getState();
        savedInstanceState.putInt(BUNDLE_ARGUMENT_PARENT_CATEGORY_ID, state.getParentCategoryId());
        Parcelable categoriesState = Parcels.wrap(state.getCategories());
        savedInstanceState.putParcelable(BUNDLE_ARGUMENT_CATEGORIES, categoriesState);
        Parcelable categoriesListState = categoriesListView.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(BUNDLE_ARGUMENT_CATEGORY_LIST_STATE, categoriesListState);
    }

    private CategoriesContract.State readStateFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return null;
        }

        int parentCategoryId = -1;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_PARENT_CATEGORY_ID)) {
            parentCategoryId = savedInstanceState.getInt(BUNDLE_ARGUMENT_PARENT_CATEGORY_ID);
        }
        List<Category> categories = null;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_CATEGORIES)) {
            categories = Parcels.unwrap(savedInstanceState.getParcelable(BUNDLE_ARGUMENT_CATEGORIES));
        }
        Parcelable categoriesListState = null;
        if (savedInstanceState.containsKey(BUNDLE_ARGUMENT_CATEGORY_LIST_STATE)) {
            categoriesListState = savedInstanceState.getParcelable(BUNDLE_ARGUMENT_CATEGORY_LIST_STATE);
        }
        return new CategoriesState(parentCategoryId, categories,
                new RecyclerViewState(categoriesListState));
    }
}
