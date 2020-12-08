package com.cw.secondapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BusinessContentProvider extends ContentProvider {
    public static final String TAG = "jcw";
    Context mContext;
    SelfOpenHelper mSelfOpenHelper;

    @Override
    public boolean onCreate() {

        // 人为制造crash
        mContext = getContext();
        mSelfOpenHelper = new SelfOpenHelper(mContext, "oufeng", null, 1);
//        if (null != mContext) {
//            mContext = null;
//        }
//        mContext.getPackageName();
        try {
            throw new NullPointerException("onCreate fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
//        getCallingPackage();
        List<String> segments = uri.getPathSegments();
        if (segments.isEmpty()) {
            Log.e("LOG_TAG", "getPathSegments error");
            return null;
        }

        SQLiteDatabase readableDatabase = mSelfOpenHelper.getReadableDatabase();
        if (null != readableDatabase) {
            return readableDatabase.query(segments.get(0), null, selection, selectionArgs, null, null, sortOrder);
        } else {
            Log.e("LOG_TAG", "getReadableDatabase response null");
            return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Log.d(TAG, "Entry call provider: " + method);
//        try {
//            throw new NullPointerException("call fake exception for print callstack");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        getContext().getContentResolver().notifyChange(Uri.parse("content://businessprovider.authorities/descendant"), null);

        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < 10; i++) {
            contentValues.clear();
            contentValues.put(KEYNAME, "key" + i + INDEX);
            ++INDEX;
            contentValues.put(KEYVALUE, String.valueOf(i));
            mSelfOpenHelper.getWritableDatabase().insert(TABLE_HUNAN, null, contentValues);
        }


//        // verify is support concurrence start
//        {
//            // section for use getReadableDatabase or getWritableDatabase
////            mSelfOpenHelper.getReadableDatabase().execSQL("");
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        {
//            // section for data parse
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        // verify is support concurrence end

        Log.d(TAG, "End call provider: " + method);
        return super.call(method, arg, extras);
    }

    static final String TABLE_HUNAN = "hunan";
    static final String TABLE_SHANGHAI = "shanghai";
    static final String TABLE_GUANXI = "guanxi";
    static final String KEYNAME = "name";
    static final String KEYVALUE = "value";
    public static int INDEX = 0;

    static private class SelfOpenHelper extends SQLiteOpenHelper {
        public SelfOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_HUNAN +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE ON CONFLICT REPLACE NOT NULL, value TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SHANGHAI +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE ON CONFLICT REPLACE NOT NULL, value TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_GUANXI +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE ON CONFLICT REPLACE NOT NULL, value TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
