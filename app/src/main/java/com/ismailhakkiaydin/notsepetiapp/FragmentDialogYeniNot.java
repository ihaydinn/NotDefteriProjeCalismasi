package com.ismailhakkiaydin.notsepetiapp;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ismailhakkiaydin.notsepetiapp.data.NotlarProvider;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

public class FragmentDialogYeniNot extends DialogFragment {

    static final Uri CONTENT_URI = NotlarProvider.CONTENT_URI;

    private ImageButton mBtnKapat;
    private EditText mNotIcerik;
    private TarihDatePicker mNotTarih;
    private Button mNotEkle;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogTemasi);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dialog_yeni_not,container, false);

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnKapat = view.findViewById(R.id.btn_dialog_kapat);
        mNotIcerik = view.findViewById(R.id.et_not);
        mNotTarih = view.findViewById(R.id.dp_tarih);
        mNotEkle = view.findViewById(R.id.btn_not_ekle);

        mBtnKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mNotEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, mNotTarih.getDayOfMonth());
                calendar.set(Calendar.MONTH, mNotTarih.getMonth());
                calendar.set(Calendar.YEAR, mNotTarih.getYear());

                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);*/

                ContentValues contentValues = new ContentValues();
                contentValues.put("notIcerik", mNotIcerik.getText().toString());
               // contentValues.put("notTarih", calendar.getTimeInMillis());
                contentValues.put("notTarih", mNotTarih.getTime());
                Uri uri = getActivity().getContentResolver().insert(CONTENT_URI, contentValues);
                Toast.makeText(getContext(), "Eklendi : " +uri, Toast.LENGTH_LONG).show();

                EventBus.getDefault().post(new DataEvent.DataGuncelle(1));
                dismiss();

            }
        });

    }
}
