package com.ismailhakkiaydin.notsepetiapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NotlarProvider extends ContentProvider {

    SQLiteDatabase db;

    static final String CONTENT_AUTHORITY = "com.ismailhakkiaydin.notsepetiapp.notlarprovider";
    static final String PATH_NOTLAR = "notlar";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTLAR);

    static final UriMatcher matcher;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CONTENT_AUTHORITY, PATH_NOTLAR, 1);
    }

    private final static String DATABASE_NAME = "notlar.db";
    private final static int DATABASE_VERSION = 1;
    private final static String NOTLAR_TABLE_NAME = "notlar";
    private final static String CREATE_NOTLAR_TABLE = " CREATE TABLE " + NOTLAR_TABLE_NAME
            + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " notIcerik TEXT NOT NULL, "
            + " notTarih TEXT, "
            + " tamamlandi INTEGER DEFAULT 0);";

    @Override
    public boolean onCreate() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        db = databaseHelper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        Cursor cursor = null;

        switch (matcher.match(uri)){
            case 1:
                cursor = db.query(NOTLAR_TABLE_NAME, strings, s, strings1, s1, null, null);
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        switch (matcher.match(uri)){
            case 1:
                Long eklenenID = db.insert(NOTLAR_TABLE_NAME, null, contentValues);
                if (eklenenID>0){
                    Uri _uri = ContentUris.withAppendedId(CONTENT_URI, eklenenID);
                    return _uri;
                }
                break;
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            sqLiteDatabase.execSQL(CREATE_NOTLAR_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            db.execSQL(" DROP TABLE IF EXISTS " + NOTLAR_TABLE_NAME);
            onCreate(db);

        }
    }

}
