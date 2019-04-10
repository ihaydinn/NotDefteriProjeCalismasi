package com.ismailhakkiaydin.notsepetiapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class FragmentDialogTamamla extends DialogFragment {

    private ImageButton mBtnKapat;
    private Button mTamamlandi;
    static  int notAdapterPosition=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogTemasi);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_tamamlandi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnKapat = view.findViewById(R.id.btn_dialog_kapat_2);
        mBtnKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mTamamlandi = view.findViewById(R.id.btn_tamamlandi);
        mTamamlandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new DataEvent.NotTamamlaPosition(notAdapterPosition));
                dismiss();
            }
        });
    }

    @Subscribe (sticky = true)
    public void onDialogTamamlaNotPosition(DataEvent.TamamlanacakNotPosition event){
        notAdapterPosition = event.getPosition();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
