package com.cw.secondapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BusinessContentProvider extends ContentProvider {
    Context mContext;
    @Override
    public boolean onCreate() {

        // 人为制造crash
        mContext = getContext();
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
        return null;
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
        try {
            throw new NullPointerException("call fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getContext().getContentResolver().notifyChange(Uri.parse("content://businessprovider.authorities/descendant"), null);

        return super.call(method, arg, extras);
    }
}
