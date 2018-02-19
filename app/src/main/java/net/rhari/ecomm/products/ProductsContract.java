package net.rhari.ecomm.products;

import net.rhari.ecomm.base.BasePresenter;
import net.rhari.ecomm.base.BaseState;
import net.rhari.ecomm.base.BaseView;
import net.rhari.ecomm.data.model.Product;
import net.rhari.ecomm.util.ListState;

import java.util.List;

interface ProductsContract {

    interface View extends BaseView {

        void updateProductsList(List<Product> products);

        void goToVariantsPage(int productId);

        void showLoadingContentIndicator();

        void hideLoadingContentIndicator();

        void restoreListState(ListState state);

        void showErrorMessage(String errorMessage);

        void hideErrorMessage();
    }

    interface State extends BaseState {

        int getCategoryId();

        List<Product> getProducts();

        ListState getProductsListState();
    }

    interface Presenter extends BasePresenter<View, State> {

        void setCategoryId(int categoryId);

        void onListItemClick(Product product, int position);

        void onRetry();
    }
}
