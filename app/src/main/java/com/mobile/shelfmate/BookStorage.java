package com.mobile.shelfmate;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookStorage {
    private static final String PREF_NAME = "ShelfMatePrefs";
    private static final String KEY_BOOKS = "books";

    public static void saveBooks(Context context, List<Book> books) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(books);
        editor.putString(KEY_BOOKS, json);
        editor.apply();
    }

    public static List<Book> loadBooks(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_BOOKS, null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>() {}.getType();
        if (json != null) {
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }
}
