package net.rhari.ecomm.base;

public interface BasePresenter<V extends BaseView> {

    void subscribe(V view);

    void unsubscribe();
}