package net.rhari.ecomm.variants;

import net.rhari.ecomm.base.BasePresenter;
import net.rhari.ecomm.base.BaseState;
import net.rhari.ecomm.base.BaseView;
import net.rhari.ecomm.data.model.Variant;
import net.rhari.ecomm.util.ListState;

import java.util.List;

interface VariantsContract {

    interface View extends BaseView {

        void updateVariantList(List<Variant> variants);

        void showLoadingContentIndicator();

        void hideLoadingContentIndicator();

        void restoreListState(ListState state);

        void showErrorMessage();

        void hideErrorMessage();
    }

    interface State extends BaseState {

        int getProductId();

        List<Variant> getVariants();

        ListState getVariantsListState();
    }

    interface Presenter extends BasePresenter<View, State> {

        void setProductId(int productId);

        void onRetry();
    }
}