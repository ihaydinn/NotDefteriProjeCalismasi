package com.ismailhakkiaydin.notsepetiapp.data;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NotlarRecyclerView extends RecyclerView {

    List<View> elemanYoksaSaklanacaklar = Collections.emptyList();
    List<View> elemanYoksaGosterilecekler = Collections.emptyList();

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {

            viewGosterGizle();

        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            viewGosterGizle();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            viewGosterGizle();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            viewGosterGizle();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            viewGosterGizle();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            viewGosterGizle();
        }
    };

    private void viewGosterGizle() {

        if (getAdapter() != null && !elemanYoksaSaklanacaklar.isEmpty() && !elemanYoksaGosterilecekler.isEmpty()){
            if (getAdapter().getItemCount() == 0){
                for (View view : elemanYoksaSaklanacaklar){
                    view.setVisibility(View.GONE);
                }
                setVisibility(View.GONE);
                for (View view : elemanYoksaGosterilecekler){
                    view.setVisibility(View.VISIBLE);
                }
            }else if (getAdapter().getItemCount() > 1){
                for (View view : elemanYoksaSaklanacaklar){
                    view.setVisibility(View.VISIBLE);
                }
                setVisibility(View.VISIBLE);
                for (View view : elemanYoksaGosterilecekler){
                    view.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null){
            adapter.registerAdapterDataObserver(mObserver);
            mObserver.onChanged();
        }
    }

    public NotlarRecyclerView(Context context) {
        super(context);
    }

    public NotlarRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NotlarRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void egerDataYoksaSaklanacaklar(View... saklanacakViews) {

        elemanYoksaSaklanacaklar = Arrays.asList(saklanacakViews);

    }

    public void egerDataYoksaSGosterilecekler(View gosterilecekViews) {

        elemanYoksaGosterilecekler = Arrays.asList(gosterilecekViews);

    }
}
