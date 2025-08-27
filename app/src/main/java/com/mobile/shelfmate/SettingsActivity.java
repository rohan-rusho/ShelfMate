package com.mobile.shelfmate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private Button btnBackup, btnReset, btnAddDefaultBooks;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "BookPrefs";
    private static final String BOOKS_KEY = "books";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnBackup = findViewById(R.id.btnBackup);
        btnReset = findViewById(R.id.btnReset);
        btnAddDefaultBooks = findViewById(R.id.btnAddDefaultBooks);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        btnBackup.setOnClickListener(v -> backupBooks());

        btnReset.setOnClickListener(v -> resetBooks());

        btnAddDefaultBooks.setOnClickListener(v -> addDefaultBooks());
    }

    // Backup books to CSV
    private void backupBooks() {
        String json = sharedPreferences.getString(BOOKS_KEY, null);
        if (json == null) {
            Toast.makeText(this, "No books to backup", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Book> bookList = new Gson().fromJson(json, new TypeToken<ArrayList<Book>>() {}.getType());
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Title,Author,Total Pages,Current Page,Status\n");
        for (Book b : bookList) {
            csvBuilder.append(b.getTitle()).append(",");
            csvBuilder.append(b.getAuthor()).append(",");
            csvBuilder.append(b.getTotalPages()).append(",");
            csvBuilder.append(b.getCurrentPage()).append(",");
            csvBuilder.append(b.getStatus()).append("\n");
        }

        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!path.exists()) path.mkdirs();

            File file = new File(path, "books_backup.csv");
            FileWriter writer = new FileWriter(file);
            writer.write(csvBuilder.toString());
            writer.close();

            Toast.makeText(this, "Backup saved to Downloads/books_backup.csv", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Backup failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Reset books
    private void resetBooks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(BOOKS_KEY);
        editor.apply();
        Toast.makeText(this, "All books have been reset.\nAdd some books to see the list.", Toast.LENGTH_SHORT).show();
    }

    // Add 5 default books manually
    private void addDefaultBooks() {
        ArrayList<Book> defaultBooks = new ArrayList<>();
        defaultBooks.add(new Book("The Alchemist", "Paulo Coelho", 197, 50, "Reading", "Fiction"));
        defaultBooks.add(new Book("1984", "George Orwell", 328, 328, "Completed", "Dystopian"));
        defaultBooks.add(new Book("To Kill a Mockingbird", "Harper Lee", 281, 0, "Not Started", "Fiction"));
        defaultBooks.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", 180, 0, "Not Started", "Classic"));
        defaultBooks.add(new Book("Harry Potter", "J.K. Rowling", 400, 200, "Reading", "Fantasy"));


        // Save to SharedPreferences
        String json = new Gson().toJson(defaultBooks);
        sharedPreferences.edit().putString(BOOKS_KEY, json).apply();

        Toast.makeText(this, "5 Default Books Added!", Toast.LENGTH_SHORT).show();
    }
}
