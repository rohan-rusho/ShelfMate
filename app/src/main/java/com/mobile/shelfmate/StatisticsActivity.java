package com.mobile.shelfmate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private TextView tvTotalBooks, tvBooksInProgress, tvPagesRead, tvFavoriteGenre;
    private ProgressBar progressOverall;
    private ArrayList<Book> bookList;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "BookPrefs";
    private static final String BOOKS_KEY = "books";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        tvTotalBooks = findViewById(R.id.tvTotalBooks);
        tvBooksInProgress = findViewById(R.id.tvBooksInProgress);
        tvPagesRead = findViewById(R.id.tvPagesRead);
        tvFavoriteGenre = findViewById(R.id.tvFavoriteGenre);
        progressOverall = findViewById(R.id.progressOverall);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadBooks();
        updateStatistics();
    }

    private void loadBooks() {
        String json = sharedPreferences.getString(BOOKS_KEY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Book>>() {}.getType();
            bookList = new Gson().fromJson(json, type);
        } else {
            bookList = new ArrayList<>();
        }
    }

    private void updateStatistics() {
        int totalBooks = bookList.size();
        int booksInProgress = 0;
        int totalPages = 0;
        int pagesRead = 0;

        Map<String, Integer> genreCount = new HashMap<>();

        for (Book book : bookList) {
            totalPages += book.getTotalPages();
            pagesRead += book.getCurrentPage();

            if (book.getStatus().equalsIgnoreCase("Reading")) {
                booksInProgress++;
            }

            // Count genres for favorite
            String genre = book.getGenre(); // Add a genre field in Book class
            if (genre != null && !genre.isEmpty()) {
                genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
            }
        }

        tvTotalBooks.setText("Total Books: " + totalBooks);
        tvBooksInProgress.setText("Books in Progress: " + booksInProgress);
        tvPagesRead.setText("Pages Read: " + pagesRead);

        int overallProgress = (totalPages == 0) ? 0 : (pagesRead * 100 / totalPages);
        progressOverall.setProgress(overallProgress);

        // Find favorite genre
        String favoriteGenre = "N/A";
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : genreCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                favoriteGenre = entry.getKey();
            }
        }
        tvFavoriteGenre.setText("Favorite Genre: " + favoriteGenre);
    }
}
