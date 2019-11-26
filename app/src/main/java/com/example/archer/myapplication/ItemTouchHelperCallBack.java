package com.example.archer.myapplication;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by archer on 2018/5/10.
 */

public class ItemTouchHelperCallBack extends ItemTouchHelper.Callback {
    private ItemTouchHelperAdaptor Myadaptor;
    public ItemTouchHelperCallBack(ItemTouchHelperAdaptor madaptor){
        Myadaptor=madaptor;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags=ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags=ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Myadaptor.onItemMove(viewHolder,target);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Myadaptor.onItemDissmiss(viewHolder.getAdapterPosition());
    }




}
