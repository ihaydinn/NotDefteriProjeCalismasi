package com.ismailhakkiaydin.notsepetiapp;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TarihDatePicker extends LinearLayout implements View.OnTouchListener {

    TextView mTextGun;
    TextView mTextAy;
    TextView mTextYil;
    Calendar mCalender;
    SimpleDateFormat mFormatter;
    
    public TarihDatePicker(Context context) {
        super(context);
        init(context);
    }

    public TarihDatePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TarihDatePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tarih_view, this);
        mCalender = Calendar.getInstance();
        mFormatter = new SimpleDateFormat("MMM");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextGun = this.findViewById(R.id.tv_tarih_gun);
        mTextAy = this.findViewById(R.id.tv_tarih_ay);
        mTextYil = this.findViewById(R.id.tv_tarih_yil);

        mTextGun.setOnTouchListener(this);
        mTextAy.setOnTouchListener(this);
        mTextYil.setOnTouchListener(this);

        int gun = mCalender.get(Calendar.DATE);
        int ay = mCalender.get(Calendar.MONTH);
        int yil = mCalender.get(Calendar.YEAR);

        guncelle(gun, ay, yil, 0,0,0);

    }

    private void guncelle(int gun, int ay, int yil, int saat, int dakika, int saniye) {

        mTextGun.setText(""+gun);
        mTextAy.setText(mFormatter.format(mCalender.getTime()));
        mTextYil.setText(""+yil);

    }

    public long getTime(){
        return mCalender.getTimeInMillis();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (view.getId()){

            case R.id.tv_tarih_gun:
                islemYap(mTextGun, motionEvent);
                break;

            case R.id.tv_tarih_ay:
                islemYap(mTextAy, motionEvent);
                break;

            case R.id.tv_tarih_yil:
                islemYap(mTextYil, motionEvent);
                break;
        }

        return true;
    }

    private void islemYap(TextView textView, MotionEvent motionEvent) {

        /*
        * SOL 0 YUKARI 1 SAĞ 2 AŞAĞI 3 İLE İFADE EDİLİR
        * */

        Drawable[] drawables = textView.getCompoundDrawables();

        if (yukariDrawable(drawables) && asagiDrawable(drawables)){

            Rect yukariSinir = drawables[1].getBounds();
            Rect asagiSinir = drawables[3].getBounds();

            float x = motionEvent.getX();
            float y = motionEvent.getY();

            if (yukariDrawableTiklandi(textView, yukariSinir, x, y)){

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    arttir(textView.getId());
                }

            }else if (asagiDrawableTiklandi(textView, asagiSinir, x, y)){

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    azalt(textView.getId());

                }

            }else {

            }

        }

    }

    private void azalt(int id) {

        switch (id){
            case R.id.tv_tarih_gun:
                mCalender.add(Calendar.DATE, -1);
                break;

            case R.id.tv_tarih_ay:
                mCalender.add(Calendar.MONTH, -1);
                break;

            case R.id.tv_tarih_yil:
                mCalender.add(Calendar.YEAR, -1);
                break;
        }
        tarihleriGuncelle(mCalender);
    }

    private void arttir(int id) {

        switch (id){
            case R.id.tv_tarih_gun:
                mCalender.add(Calendar.DATE, 1);
                break;

            case R.id.tv_tarih_ay:
                mCalender.add(Calendar.MONTH, 1);
                break;

            case R.id.tv_tarih_yil:
                mCalender.add(Calendar.YEAR, 1);
                break;
        }
        tarihleriGuncelle(mCalender);
    }

    private void tarihleriGuncelle(Calendar mCalender) {

        int gun = mCalender.get(Calendar.DATE);
       // int ay = mCalender.get(Calendar.MONTH);
        int yil = mCalender.get(Calendar.YEAR);

        mTextGun.setText(""+gun);
        mTextAy.setText(mFormatter.format(mCalender.getTime()));
        mTextYil.setText(""+yil);

    }


    private boolean asagiDrawableTiklandi(TextView textView, Rect asagiSinir, float x, float y) {

        int xMin = textView.getPaddingLeft();
        int xMax = textView.getWidth() - textView.getPaddingRight();
        int yMax = textView.getHeight() - textView.getPaddingBottom();
        int yMin = yMax - asagiSinir.height();

        return x > xMin && x < xMax && y > yMin && y < yMax;

    }

    private boolean yukariDrawableTiklandi(TextView textView, Rect yukariSinir, float x, float y) {

        int xMin = textView.getPaddingLeft();
        int xMax = textView.getWidth() - textView.getPaddingRight();
        int yMin = textView.getPaddingTop();
        int yMax = textView.getPaddingTop() + yukariSinir.height();

        return x > xMin && x < xMax && y > yMin && y < yMax;

    }

    private boolean yukariDrawable(Drawable[] drawables){
        return drawables[1] != null;
    }

    private boolean asagiDrawable(Drawable[] drawables){
        return drawables[3] != null;
    }

}
