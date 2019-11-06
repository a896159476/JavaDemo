package com.eshuix.javademo.base.recycler.multi;

import androidx.annotation.NonNull;

import com.eshuix.javademo.base.recycler.BaseRecyclerViewAdapter;
import com.eshuix.javademo.base.recycler.BaseViewHolder;

import java.util.List;

public abstract class BaseMultiItemAdapter <T extends BaseMultiItemEntity, K extends BaseViewHolder> extends BaseRecyclerViewAdapter<T,K> {

    public BaseMultiItemAdapter(@NonNull List<BaseMultiItemEntity> data) {
        super(data);
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getItemType();
    }
}
