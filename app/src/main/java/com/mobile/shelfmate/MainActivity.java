package com.mobile.shelfmate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvBooks;
    private ImageButton btnAddBook, btnSettings, btnStatistics;
    private TextView tvEmptyMessage;
    private ArrayList<Book> bookList;
    private BookAdapter bookAdapter;

    private static final int ADD_BOOK_REQUEST = 100;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "BookPrefs";
    private static final String BOOKS_KEY = "books";
    private static final String FIRST_LAUNCH_KEY = "firstLaunch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvBooks = findViewById(R.id.rvBooks);
        btnAddBook = findViewById(R.id.btnAddBook);
        btnSettings = findViewById(R.id.btnSettings);
        btnStatistics = findViewById(R.id.btnStatistics);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        loadBooks();

        bookAdapter = new BookAdapter(this, bookList);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        rvBooks.setAdapter(bookAdapter);

        updateEmptyMessage();

        btnAddBook.setOnClickListener(v ->
                startActivityForResult(new Intent(MainActivity.this, AddBookActivity.class), ADD_BOOK_REQUEST)
        );

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SettingsActivity.class))
        );

        btnStatistics.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class))
        );
    }

    private void loadBooks() {
        boolean firstLaunch = sharedPreferences.getBoolean(FIRST_LAUNCH_KEY, true);

        if (firstLaunch) {
            // Add 5 default books
            bookList = new ArrayList<>();
            bookList.add(new Book("The Alchemist", "Paulo Coelho", 197, 50, "Reading", "Fiction"));
            bookList.add(new Book("1984", "George Orwell", 328, 328, "Completed", "Fiction"));
            bookList.add(new Book("To Kill a Mockingbird", "Harper Lee", 281, 0, "Not Started", "Fiction"));
            bookList.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", 180, 0, "Not Started", "Fiction"));
            bookList.add(new Book("Harry Potter", "J.K. Rowling", 400, 200, "Reading", "Fantasy"));


            saveBooks();

            sharedPreferences.edit().putBoolean(FIRST_LAUNCH_KEY, false).apply();
        } else {
            String json = sharedPreferences.getString(BOOKS_KEY, null);
            if (json != null) {
                Type type = new TypeToken<ArrayList<Book>>() {}.getType();
                bookList = new Gson().fromJson(json, type);
            } else {
                bookList = new ArrayList<>();
            }
        }
    }

    private void saveBooks() {
        String json = new Gson().toJson(bookList);
        sharedPreferences.edit().putString(BOOKS_KEY, json).apply();
        updateEmptyMessage();
    }

    private void updateEmptyMessage() {
        if (bookList.isEmpty()) {
            tvEmptyMessage.setVisibility(View.VISIBLE);
            rvBooks.setVisibility(View.GONE);
        } else {
            tvEmptyMessage.setVisibility(View.GONE);
            rvBooks.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_BOOK_REQUEST && resultCode == RESULT_OK && data != null) {
            Book newBook = (Book) data.getSerializableExtra("newBook");
            if (newBook != null) {
                bookList.add(newBook);
                saveBooks();
                bookAdapter.notifyItemInserted(bookList.size() - 1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
        bookAdapter = new BookAdapter(this, bookList);
        rvBooks.setAdapter(bookAdapter);
        updateEmptyMessage();
    }
}
