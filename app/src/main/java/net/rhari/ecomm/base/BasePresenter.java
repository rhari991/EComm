package net.rhari.ecomm.base;

public interface BasePresenter<V extends BaseView, S extends BaseState> {

    void subscribe(V view, S state);

    void unsubscribe();

    S getState();
}