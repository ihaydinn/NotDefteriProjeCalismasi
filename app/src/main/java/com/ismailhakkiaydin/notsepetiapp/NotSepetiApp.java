package com.ismailhakkiaydin.notsepetiapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class NotSepetiApp extends Application {

    public static void sharedFiltre(Context context, int secilenFiltre){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("filtre", secilenFiltre);
        editor.apply();
    }

    public static int sharedOku(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int secilenFiltre = sharedPreferences.getInt("filtre", 0);
        return secilenFiltre;
    }

}
