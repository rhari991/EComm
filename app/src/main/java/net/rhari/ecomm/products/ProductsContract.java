package net.rhari.ecomm.products;

import net.rhari.ecomm.base.BasePresenter;
import net.rhari.ecomm.base.BaseState;
import net.rhari.ecomm.base.BaseView;
import net.rhari.ecomm.data.model.RankingInfo;
import net.rhari.ecomm.data.model.SortedProduct;
import net.rhari.ecomm.util.ListState;

import java.util.List;

interface ProductsContract {

    interface View extends BaseView {

        void updateSortOrderOptions(List<RankingInfo> sortOrderOptions);

        void updateProductsList(List<SortedProduct> products, RankingInfo sortOrder);

        void goToVariantsPage(int productId);

        void showLoadingContentIndicator();

        void hideLoadingContentIndicator();

        void restoreListState(ListState state);

        void showErrorMessage(String errorMessage);

        void hideErrorMessage();
    }

    interface State extends BaseState {

        int getCategoryId();

        RankingInfo getCurrentSortOrder();

        List<RankingInfo> getSortOrderOptions();

        List<SortedProduct> getProducts();

        ListState getProductsListState();
    }

    interface Presenter extends BasePresenter<View, State> {

        void setCategoryId(int categoryId);

        void onSortOrderChange(RankingInfo newOrder);

        void onListItemClick(SortedProduct product, int position);

        void onRetry();
    }
}
