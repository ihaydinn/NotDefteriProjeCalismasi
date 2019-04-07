package com.ismailhakkiaydin.notsepetiapp.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ismailhakkiaydin.notsepetiapp.R;
import com.ismailhakkiaydin.notsepetiapp.data.Notlar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterNotlarListesi extends RecyclerView.Adapter<AdapterNotlarListesi.NotHolder> {

    LayoutInflater mInflater;
    ArrayList<Notlar> tumNotlar;

    public AdapterNotlarListesi(Context context, ArrayList<Notlar> notlar){
        mInflater = LayoutInflater.from(context);
        tumNotlar = notlar;
    }

    @NonNull
    @Override
    public NotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.tek_satir_not, parent, false);
        NotHolder holder = new NotHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotHolder holder, int position) {
        holder.mTextNotIcerik.setText(tumNotlar.get(position).getNotIcerik());
        holder.mTextNotTarih.setText(" " + tumNotlar.get(position).getId());

    }

    @Override
    public int getItemCount() {
        return tumNotlar.size();
    }

    public static class NotHolder extends RecyclerView.ViewHolder{

        TextView mTextNotIcerik;
        TextView mTextNotTarih;

        public NotHolder(View itemView) {
            super(itemView);

            mTextNotIcerik = itemView.findViewById(R.id.tv_not_icerik);
            mTextNotTarih = itemView.findViewById(R.id.tv_not_tarih);

        }
    }

}
