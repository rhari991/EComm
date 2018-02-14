package net.rhari.ecomm.base;

public interface BaseStatefulPresenter<V extends BaseView, S extends BaseState> extends BasePresenter<V> {

    void subscribe(V view, S state);

    S getState();
}