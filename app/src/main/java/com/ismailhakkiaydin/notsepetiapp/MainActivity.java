package com.ismailhakkiaydin.notsepetiapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ismailhakkiaydin.notsepetiapp.adapter.AdapterNotlarListesi;
import com.ismailhakkiaydin.notsepetiapp.adapter.Divider;
import com.ismailhakkiaydin.notsepetiapp.adapter.Filtreler;
import com.ismailhakkiaydin.notsepetiapp.adapter.SimpleTouchCallback;
import com.ismailhakkiaydin.notsepetiapp.data.Notlar;
import com.ismailhakkiaydin.notsepetiapp.data.NotlarProvider;
import com.ismailhakkiaydin.notsepetiapp.data.NotlarRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final Uri CONTENT_URI = NotlarProvider.CONTENT_URI;
    static final String SIRALAMA_ONEMSIZ = "SIRALAMA ONEMSIZ";
    static final String TAMAMLANMA_ONEMSIZ = "TAMAMLANMA ONEMSIZ";

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
        mRecyclerViewNotlar.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        mRecyclerViewNotlar.egerDataYoksaSaklanacaklar(mToolbar);
        mRecyclerViewNotlar.egerDataYoksaSGosterilecekler(bosListe);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerViewNotlar.setLayoutManager(manager);
        mAdapterNotlarListesi = new AdapterNotlarListesi(this, tumNotlar);
        mAdapterNotlarListesi.setHasStableIds(true);
        mRecyclerViewNotlar.setAdapter(mAdapterNotlarListesi);

        //swipe işlemi
        SimpleTouchCallback callback = new SimpleTouchCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerViewNotlar);

        int secilenFiltre = NotSepetiApp.sharedOku(this);
        switch (secilenFiltre){
            case 0:
                dataGuncelle(SIRALAMA_ONEMSIZ, TAMAMLANMA_ONEMSIZ);
                break;
            case 1:
                NotSepetiApp.sharedFiltre(this, Filtreler.COK_VAKIT_VAR);
                dataGuncelle("notTarih DESC", TAMAMLANMA_ONEMSIZ);
                break;
            case 2:
                NotSepetiApp.sharedFiltre(this, Filtreler.AZ_VAKIT_VAR);
                dataGuncelle("notTarih ASC", TAMAMLANMA_ONEMSIZ);
                break;
            case 3:
                NotSepetiApp.sharedFiltre(this, Filtreler.TAMAMLANANLAR);
                dataGuncelle(SIRALAMA_ONEMSIZ, "1");
                break;
            case 4:
                NotSepetiApp.sharedFiltre(this, Filtreler.TAMAMLANMAYANLAR);
                dataGuncelle(SIRALAMA_ONEMSIZ, "0");
                break;
        }

        backgroundResminiYerlestir();

    }

    public void dataGuncelle(String siralama, String tamamlanma) {

        tumNotlar.clear();
        tumNotlar = tumNotlariGetir(siralama, tamamlanma);
        mAdapterNotlarListesi.update(tumNotlar);

    }

    private ArrayList<Notlar> tumNotlariGetir(String siralama, String tamamlanma){

        String siralamaSorgusu = siralama;
        String selection = "tamamlandi=?";
        String[] tamamlanmaSorgusu = {tamamlanma};

        if (siralama.equals(SIRALAMA_ONEMSIZ)){
            siralamaSorgusu = null;
        }
        if (tamamlanma.equals(TAMAMLANMA_ONEMSIZ)){
            selection = null;
            tamamlanmaSorgusu = null;
        }

        Cursor cursor = getContentResolver().query(CONTENT_URI, new String[]{"id", "notIcerik", "notTarih", "tamamlandi"}, selection, tamamlanmaSorgusu, siralamaSorgusu);

        if (cursor != null){
            while (cursor.moveToNext()){
                Notlar geciciNot = new Notlar();
                geciciNot.setId(cursor.getInt(cursor.getColumnIndex("id")));
                geciciNot.setNotIcerik(cursor.getString(cursor.getColumnIndex("notIcerik")));
                geciciNot.setNotTarih(cursor.getLong(cursor.getColumnIndex("notTarih")));
                geciciNot.setTamamlandi(cursor.getInt(cursor.getColumnIndex("tamamlandi")));
                tumNotlar.add(geciciNot);
            }
        }
        return tumNotlar;
    }

    private void notEkleDialogGoster() {

        FragmentDialogYeniNot dialog = new FragmentDialogYeniNot();
        dialog.show(getSupportFragmentManager(),"DialogYeniNot");

    }

    private void notTamamlaDialogGoster(int position) {

        EventBus.getDefault().postSticky(new DataEvent.TamamlanacakNotPosition(position));
        FragmentDialogTamamla dialog = new FragmentDialogTamamla();
        dialog.show(getSupportFragmentManager(), "DialogNotTamamla");

    }

    private void backgroundResminiYerlestir() {

        ImageView background = findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.back)
                .apply(new RequestOptions().centerCrop())
                .into(background);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        boolean sonuc = true;

        switch (id){
            case R.id.menu_yeninot:
                notEkleDialogGoster();
                break;
            case R.id.menu_eklenmesirasi:
                NotSepetiApp.sharedFiltre(this, Filtreler.NO_FILTER);
                dataGuncelle(SIRALAMA_ONEMSIZ, TAMAMLANMA_ONEMSIZ);
                break;
            case R.id.menu_cokvakit:
                NotSepetiApp.sharedFiltre(this, Filtreler.COK_VAKIT_VAR);
                dataGuncelle("notTarih DESC", TAMAMLANMA_ONEMSIZ);
                break;
            case R.id.menu_azvakit:
                NotSepetiApp.sharedFiltre(this, Filtreler.AZ_VAKIT_VAR);
                dataGuncelle("notTarih ASC", TAMAMLANMA_ONEMSIZ);
                break;
            case R.id.menu_tamamlananlar:
                NotSepetiApp.sharedFiltre(this, Filtreler.TAMAMLANANLAR);
                dataGuncelle(SIRALAMA_ONEMSIZ, "1");
                break;
            case R.id.menu_tamamlanmayanlar:
                NotSepetiApp.sharedFiltre(this, Filtreler.TAMAMLANMAYANLAR);
                dataGuncelle(SIRALAMA_ONEMSIZ, "0");
                break;
                default:
                    sonuc = false;
                    NotSepetiApp.sharedFiltre(this, Filtreler.NO_FILTER);
                    break;
        }

        return sonuc;
    }

    @Subscribe
    public void onDialogNotTamamla(DataEvent.DialogTamamlaNotPosition event){
        notTamamlaDialogGoster(event.getPosition());
    }

    @Subscribe
    public void onNotEkleDialogGoster(DataEvent.NotEkleDialogGoster event){
        if (event.getTetikle() == 1){
            notEkleDialogGoster();
        }
    }

    @Subscribe
    public void onDataGuncelle(DataEvent.DataGuncelle event){
        if (event.getTetikle() == 1){
            dataGuncelle(SIRALAMA_ONEMSIZ, TAMAMLANMA_ONEMSIZ);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
