package com.ismailhakkiaydin.notsepetiapp.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ismailhakkiaydin.notsepetiapp.R;

public class Divider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mOrientation;

    public Divider(Context context, int orientation){

        mDivider = context.getDrawable(R.drawable.divider);
        if (orientation != LinearLayoutManager.VERTICAL){
            throw new IllegalArgumentException("Gecersiz Dekorasyon!");
        }
        mOrientation = orientation;

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL){
            drawHorizontalDivider(c, parent, state);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int sol, yukari, sag, asagi;
        sol = parent.getPaddingLeft();
        sag = parent.getWidth()-parent.getPaddingRight();
        int elemanSayisi = parent.getChildCount();

        for (int i = 0; i<elemanSayisi; i++){
            if (AdapterNotlarListesi.FOOTER != parent.getAdapter().getItemViewType(i)){
                View suakiView = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) suakiView.getLayoutParams();
                yukari = suakiView.getTop()-params.topMargin;
                asagi = yukari + mDivider.getIntrinsicHeight();
                mDivider.setBounds(sol, yukari, sag, asagi);
                mDivider.draw(c);
            }

        }

    }


}
