package com.example.test.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.model.NewsItem;

import java.net.URI;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.google.android.material.card.MaterialCardView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final Context context;
    private List<NewsItem> newsList;

    public NewsAdapter(Context context, List<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    public void updateData(List<NewsItem> newData) {
        this.newsList = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem news = newsList.get(position);

        holder.tvTitle.setText(news.getTitle());
        holder.tvBy.setText("by " + news.getBy());
        holder.tvScore.setText(String.valueOf(news.getScore()));
        holder.tvTime.setText(getRelativeTime(news.getTime()));

        String domain = extractDomain(news.getUrl());
        holder.tvDomain.setText(domain);
        holder.tvDomain.setVisibility(domain.isEmpty() ? View.GONE : View.VISIBLE);

        holder.card.setOnClickListener(v -> {
            String url = news.getUrl();
            if (url != null && !url.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager)
                        context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("URL", url);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRelativeTime(long timestamp) {
        long now = System.currentTimeMillis() / 1000;
        long diff = now - timestamp;

        if (diff < 60) return "just now";
        if (diff < 3600) return (diff / 60) + "m ago";
        if (diff < 86400) return (diff / 3600) + "h ago";
        if (diff < 2592000) return (diff / 86400) + "d ago";
        return new SimpleDateFormat("MMM d", Locale.getDefault()).format(new Date(timestamp * 1000));
    }

    private String extractDomain(String url) {
        if (url == null || url.isEmpty()) return "";
        try {
            return URI.create(url).getHost();
        } catch (Exception e) {
            return "";
        }
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView tvTitle;
        TextView tvDomain;
        TextView tvScore;
        TextView tvBy;
        TextView tvTime;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card_item);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDomain = itemView.findViewById(R.id.tv_domain);
            tvScore = itemView.findViewById(R.id.tv_score);
            tvBy = itemView.findViewById(R.id.tv_by);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}