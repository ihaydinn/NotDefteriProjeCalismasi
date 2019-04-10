package com.ismailhakkiaydin.notsepetiapp.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ismailhakkiaydin.notsepetiapp.DataEvent;
import com.ismailhakkiaydin.notsepetiapp.NotSepetiApp;
import com.ismailhakkiaydin.notsepetiapp.R;
import com.ismailhakkiaydin.notsepetiapp.data.Notlar;
import com.ismailhakkiaydin.notsepetiapp.data.NotlarProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterNotlarListesi extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static final Uri CONTENT_URI = NotlarProvider.CONTENT_URI;

    public static final int ITEM = 0;
    public static final int FOOTER = 1;
    public static final int BOS_FILTRE = 2;
    public static final int FOOTER_EKLE = 1;
    public static final int BOS_FILTRE_EKLE = 1;

    LayoutInflater mInflater;
    ArrayList<Notlar> tumNotlar;
    private ContentResolver mResolver;
    static Context mContex;
    private int mFiltre;

    public AdapterNotlarListesi(Context context, ArrayList<Notlar> notlar){
        mContex = context;
        mResolver = context.getContentResolver();
        mInflater = LayoutInflater.from(context);
        tumNotlar = notlar;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM){
            View view = mInflater.inflate(R.layout.tek_satir_not, parent, false);
            NotHolder holder = new NotHolder(view);
            return holder;
        }else if(viewType == BOS_FILTRE){
            View view = mInflater.inflate(R.layout.bos_filtre, parent, false);
            BosFiltreHolder bosFiltreHolder = new BosFiltreHolder(view);
            return bosFiltreHolder;
        } else{
            View view = mInflater.inflate(R.layout.footer, parent, false);
            FooterHolder footerHolder = new FooterHolder(view);
            return footerHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NotHolder){
            NotHolder notHolder = (NotHolder) holder;
            Notlar geciciNot = tumNotlar.get(position);
            notHolder.mTextNotIcerik.setText(geciciNot.getNotIcerik());
            notHolder.mTextNotTarih.setText(" " + geciciNot.getNotTarih());
            notHolder.setBackgroundColor(geciciNot.getTamamlandi());
            notHolder.setTarih(geciciNot.getNotTarih());
        }
    }

    @Override
    public int getItemCount() {
        if (!tumNotlar.isEmpty()){
            return tumNotlar.size() + FOOTER_EKLE;
        }else {
            if (mFiltre == Filtreler.AZ_VAKIT_VAR || mFiltre == Filtreler.COK_VAKIT_VAR || mFiltre==Filtreler.NO_FILTER){
                return 0;
            }else{
                return FOOTER_EKLE+ BOS_FILTRE_EKLE;
            }
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < tumNotlar.size()){
            return  tumNotlar.get(position).getId();
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemViewType(int position) {
       if (!tumNotlar.isEmpty()){
           if (position < tumNotlar.size()){
               return ITEM;
           }else {
               return FOOTER;
           }
       }else {
           if (mFiltre == Filtreler.TAMAMLANANLAR || mFiltre == Filtreler.TAMAMLANMAYANLAR){
               if (position == 0){
                   return BOS_FILTRE;
               }else {
                   return FOOTER;
               }
           }else {
               return ITEM;
           }
       }
    }

    @Subscribe
    public void onNotTamamlaPosition(DataEvent.NotTamamlaPosition event){
        int position = event.getPosition();
        if (position < tumNotlar.size()){
            Notlar tamamlanacakNot = tumNotlar.get(position);
            String tamamlanacakNotId = String.valueOf(tamamlanacakNot.getId());

            ContentValues contentValues = new ContentValues();
            contentValues.put("tamamlandi", 1);
            int etkilenenSatirSayisi = mResolver.update(CONTENT_URI, contentValues, "id=?", new String[]{tamamlanacakNotId});
            if (etkilenenSatirSayisi != 0){
                tamamlanacakNot.setTamamlandi(1);
                tumNotlar.set(position, tamamlanacakNot);
                notifyDataSetChanged();
            }
        }
    }

    @Subscribe
    public void onSwipe(DataEvent.SwipeData event) {
        int position = event.getPosition();
        if (position < tumNotlar.size()){
            Notlar silinecekNot = tumNotlar.get(position);
            String silinecekNotID = String.valueOf(silinecekNot.getId());
            int etkilenenSatirSayisi = mResolver.delete(CONTENT_URI, "id=?", new String[]{silinecekNotID});

            if (etkilenenSatirSayisi != 0){
                tumNotlar.remove(silinecekNot);
                //notların hepsi silindiğinde ana sayfaya otomatik yönlendirme işlemi.
                if (tumNotlar.isEmpty() && (mFiltre == Filtreler.TAMAMLANMAYANLAR || mFiltre == Filtreler.TAMAMLANANLAR)){
                    NotSepetiApp.sharedFiltre(mContex, Filtreler.NO_FILTER);
                    EventBus.getDefault().post(new DataEvent.DataGuncelle(1));
                }

                update(tumNotlar);
            }
        }
    }

    public void update(ArrayList<Notlar> tumNotlar) {
        this.tumNotlar = tumNotlar;
        mFiltre = NotSepetiApp.sharedOku(mContex);
        notifyDataSetChanged();
    }

    public static class NotHolder extends RecyclerView.ViewHolder{

        TextView mTextNotIcerik;
        TextView mTextNotTarih;
        View mItemView;

        public NotHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTextNotIcerik = itemView.findViewById(R.id.tv_not_icerik);
            mTextNotTarih = itemView.findViewById(R.id.tv_not_tarih);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EventBus.getDefault().post(new DataEvent.DialogTamamlaNotPosition(getAdapterPosition()));

                }
            });

        }

        public void setBackgroundColor(int tamamlandi) {

            Drawable backgroundDrawable;

            if (tamamlandi == 0){
                backgroundDrawable = ContextCompat.getDrawable(mContex, R.color.colorAccent);
            }else {
                backgroundDrawable = ContextCompat.getDrawable(mContex, R.color.colorPrimary);
            }
            mItemView.setBackground(backgroundDrawable);
        }

        public void setTarih(long notTarih) {
            mTextNotTarih.setText(DateUtils.getRelativeTimeSpanString(notTarih, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,0));
        }
    }

    public class BosFiltreHolder extends RecyclerView.ViewHolder{

        public BosFiltreHolder(View itemView) {
            super(itemView);

        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder{

        Button mBtnFooterEkle;

        public FooterHolder(View itemView) {
            super(itemView);

            mBtnFooterEkle = itemView.findViewById(R.id.btn_footer);
            mBtnFooterEkle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EventBus.getDefault().post(new DataEvent.NotEkleDialogGoster(1));

                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        EventBus.getDefault().unregister(this);
    }
}
