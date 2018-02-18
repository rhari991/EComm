package net.rhari.ecomm.util;

import android.os.Parcelable;

public class RecyclerViewState implements ListState {

    private final Parcelable listState;

    public RecyclerViewState(Parcelable listState) {
        this.listState = listState;
    }

    public Parcelable getListState() {
        return listState;
    }
}
