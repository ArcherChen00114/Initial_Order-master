package com.example.archer.myapplication;

import android.support.v7.widget.RecyclerView;

/**
 * Created by archer on 2018/5/10.
 */

public interface ItemTouchHelperAdaptor {
    void onItemMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);
    void onItemDissmiss(int position);
}
