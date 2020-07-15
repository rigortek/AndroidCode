package com.cw.childrenabc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;


public class AppPreferences {

    public static final String NUMBER_OR_ALPHABET = "number_alphabet";
    public static final String BIG_OR_SMALL_ALPHABET = "big_small_alphabet";

    private Context context;

    public AppPreferences(Context context) {
        this.context = context;
    }

    public void save(String name, String value) {
        if (TextUtils.isEmpty(name) || null == value) {
            return;
        }

        SharedPreferences sp = getSettingsPreference();
        if (!value.equals(sp.getString(name, value))) {
            Editor editor = sp.edit();
            editor.putString(name, value);
            editor.commit();
        }
    }

    public String get(String name, String defValue) {
        return getSettingsPreference().getString(name, defValue);
    }

    private SharedPreferences getSettingsPreference() {
        return context.getSharedPreferences("childabc", Context.MODE_PRIVATE);
    }

}