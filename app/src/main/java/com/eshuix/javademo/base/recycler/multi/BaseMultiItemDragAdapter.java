package com.eshuix.javademo.base.recycler.multi;

import androidx.annotation.NonNull;

import com.eshuix.javademo.base.recycler.BaseViewHolder;
import com.eshuix.javademo.base.recycler.touch.BaseItemRecyclerViewAdapter;

import java.util.List;

/**
 * 可拖走滑动的多布局适配器
 */
public abstract class BaseMultiItemDragAdapter<T extends BaseMultiItemEntity, K extends BaseViewHolder>
        extends BaseItemRecyclerViewAdapter<T,K> {

    public BaseMultiItemDragAdapter(@NonNull List<BaseMultiItemEntity> data) {
        super(data);
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getItemType();
    }
}
