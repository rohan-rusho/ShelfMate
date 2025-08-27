package com.mobile.shelfmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private ArrayList<Book> books;

    public BookAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvStatus.setText(book.getStatus());

        int currentPage = book.getCurrentPage();
        int totalPages = book.getTotalPages();
        int progress = 0;

        if (totalPages > 0) {
            if (currentPage > totalPages) {
                holder.tvProgressPercent.setText("Invalid: current > total");
                holder.tvProgressPercent.setTextColor(0xFFFF0000); // Red
                currentPage = totalPages;
            } else {
                progress = (int) ((currentPage * 100.0) / totalPages);
                holder.tvProgressPercent.setText(progress + "%");
                holder.tvProgressPercent.setTextColor(0xFF000000); // Black
            }
        } else {
            holder.tvProgressPercent.setText("No pages");
            holder.tvProgressPercent.setTextColor(0xFF555555); // Gray
        }

        holder.progressBar.setProgress(progress);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvStatus, tvProgressPercent;
        ProgressBar progressBar;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvStatus = itemView.findViewById(R.id.tvBookStatus);
            progressBar = itemView.findViewById(R.id.progressBook);

            tvProgressPercent = new TextView(itemView.getContext());
            tvProgressPercent.setTextColor(0xFF000000);
            tvProgressPercent.setTextSize(12);
            ((ViewGroup) itemView).addView(tvProgressPercent);
        }
    }
}
