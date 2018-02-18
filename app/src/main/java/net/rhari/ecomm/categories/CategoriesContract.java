package net.rhari.ecomm.categories;

import net.rhari.ecomm.base.BaseState;
import net.rhari.ecomm.base.BasePresenter;
import net.rhari.ecomm.base.BaseView;
import net.rhari.ecomm.data.model.Category;
import net.rhari.ecomm.util.ListState;

import java.util.List;

interface CategoriesContract {

    interface View extends BaseView {

        void updateCategoryList(List<Category> categories);

        void goToChildCategoryPage(int categoryId);

        void showLoadingContentIndicator();

        void hideLoadingContentIndicator();

        void restoreListState(ListState state);

        void showErrorMessage(String errorMessage);

        void hideErrorMessage();
    }

    interface State extends BaseState {

        int getParentCategoryId();

        List<Category> getCategories();

        ListState getCategoriesListState();
    }

    interface Presenter extends BasePresenter<View, State> {

        void setParentCategoryId(int parentCategoryId);

        void onListItemClick(Category category, int position);

        void onRetry();
    }
}
