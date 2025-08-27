package com.mobile.shelfmate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBookActivity extends AppCompatActivity {

    private EditText etTitle, etAuthor, etTotalPages, etCurrentPage;
    private Spinner spStatus, spGenre;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etTotalPages = findViewById(R.id.etTotalPages);
        etCurrentPage = findViewById(R.id.etCurrentPage);
        spStatus = findViewById(R.id.spinnerStatus);
        spGenre = findViewById(R.id.spinnerGenre);
        btnSave = findViewById(R.id.btnSave);

        // Status options
        String[] statuses = {"Not Started", "Reading", "Completed"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(statusAdapter);

        // Genre options
        String[] genres = {"Fiction", "Non-Fiction", "Fantasy", "Dystopian", "Classic", "Biography"};
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, genres);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenre.setAdapter(genreAdapter);

        btnSave.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String totalPagesStr = etTotalPages.getText().toString().trim();
        String currentPageStr = etCurrentPage.getText().toString().trim();
        String status = spStatus.getSelectedItem().toString();
        String genre = spGenre.getSelectedItem().toString();

        if (title.isEmpty() || author.isEmpty() || totalPagesStr.isEmpty() || currentPageStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int totalPages = Integer.parseInt(totalPagesStr);
        int currentPage = Integer.parseInt(currentPageStr);

        if (currentPage > totalPages) {
            Toast.makeText(this, "Current page cannot be greater than total pages!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pass all 6 arguments to Book constructor
        Book newBook = new Book(title, author, totalPages, currentPage, status, genre);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("newBook", newBook);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
