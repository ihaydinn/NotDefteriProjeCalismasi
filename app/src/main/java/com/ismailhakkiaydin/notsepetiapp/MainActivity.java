package com.ismailhakkiaydin.notsepetiapp;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ismailhakkiaydin.notsepetiapp.adapter.AdapterNotlarListesi;
import com.ismailhakkiaydin.notsepetiapp.data.Notlar;
import com.ismailhakkiaydin.notsepetiapp.data.NotlarProvider;
import com.ismailhakkiaydin.notsepetiapp.data.NotlarRecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final Uri CONTENT_URI = NotlarProvider.CONTENT_URI;

    View bosListe;
    Toolbar mToolbar;
    Button mButtonYeniNot;
    NotlarRecyclerView mRecyclerViewNotlar;
    AdapterNotlarListesi mAdapterNotlarListesi;
    ArrayList<Notlar> tumNotlar = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bosListe = findViewById(R.id.bos_liste);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mButtonYeniNot = findViewById(R.id.btn_sepete_not_ekle);
        mButtonYeniNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notEkleDialogGoster();
            }
        });

        mRecyclerViewNotlar = findViewById(R.id.rv_not_listesi);
        mRecyclerViewNotlar.egerDataYoksaSaklanacaklar(mToolbar);
        mRecyclerViewNotlar.egerDataYoksaSGosterilecekler(bosListe);
        dataGuncelle();

        backgroundResminiYerlestir();

    }

    public void dataGuncelle() {
        tumNotlar.clear();
        tumNotlar = tumNotlariGetir();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerViewNotlar.setLayoutManager(manager);
        mAdapterNotlarListesi = new AdapterNotlarListesi(this, tumNotlar);
        mRecyclerViewNotlar.setAdapter(mAdapterNotlarListesi);

    }

    private ArrayList<Notlar> tumNotlariGetir(){
        Cursor cursor = getContentResolver().query(CONTENT_URI,new String[]{"id", "notIcerik"}, null,null,null);

        if (cursor != null){
            while (cursor.moveToNext()){
                Notlar geciciNot = new Notlar();
                geciciNot.setId(cursor.getInt(cursor.getColumnIndex("id")));
                geciciNot.setNotIcerik(cursor.getString(cursor.getColumnIndex("notIcerik")));
                tumNotlar.add(geciciNot);
            }
        }
        return tumNotlar;
    }

    private void notEkleDialogGoster() {

        FragmentDialogYeniNot dialog = new FragmentDialogYeniNot();
        dialog.show(getSupportFragmentManager(),"DialogYeniNot");

    }

    private void backgroundResminiYerlestir() {

        ImageView background = findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.back)
                .apply(new RequestOptions().centerCrop())
                .into(background);

    }
}
