package com.example.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.test.adapter.NewsAdapter;
import com.example.test.model.NewsItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvNews;
    private TextView tvStatus;
    private TextView tvCount;
    private ProgressBar progressBar;
    private Button btnLoad;
    private SwipeRefreshLayout swipeRefresh;

    private NewsAdapter newsAdapter;
    private OkHttpClient okHttpClient;
    private Gson gson;

    private static final String BASE_URL = "https://hacker-news.firebaseio.com/v0/";
    private static final String TOPSTORIES_URL = BASE_URL + "topstories.json";
    private static final String ITEM_URL = BASE_URL + "item/";
    private static final int FETCH_COUNT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvNews = findViewById(R.id.rv_news);
        tvStatus = findViewById(R.id.tv_status);
        tvCount = findViewById(R.id.tv_count);
        progressBar = findViewById(R.id.progress_bar);
        btnLoad = findViewById(R.id.btn_load);
        swipeRefresh = findViewById(R.id.swipe_refresh);

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        gson = new Gson();

        setupRecyclerView();
        setupSwipeRefresh();

        btnLoad.setOnClickListener(v -> loadNews());
    }

    private void setupRecyclerView() {
        newsAdapter = new NewsAdapter(this, new ArrayList<>(), this::openUrl);
        rvNews.setLayoutManager(new LinearLayoutManager(this));
        rvNews.setAdapter(newsAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.hn_orange);
        swipeRefresh.setOnRefreshListener(() -> {
            loadNews();
        });
    }

    private void openUrl(String url) {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }

    private void setLoading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            btnLoad.setEnabled(false);
            btnLoad.setText("Loading...");
            tvStatus.setText("Fetching news list...");
        } else {
            progressBar.setVisibility(View.GONE);
            btnLoad.setEnabled(true);
            btnLoad.setText("Load News");
            swipeRefresh.setRefreshing(false);
        }
    }

    private void loadNews() {
        setLoading(true);

        new Thread(() -> {
            List<NewsItem> newsList = new ArrayList<>();
            String errorMessage = null;

            try {
                Request request = new Request.Builder()
                        .url(TOPSTORIES_URL)
                        .build();
                Response response = okHttpClient.newCall(request).execute();
                String idsJson = response.body().string();

                Type listType = new TypeToken<List<Integer>>() {}.getType();
                List<Integer> ids = gson.fromJson(idsJson, listType);

                int count = Math.min(FETCH_COUNT, ids.size());

                // Fisher-Yates partial shuffle for distinct random items
                Random random = new Random();
                List<Integer> sampledIds = new ArrayList<>(count);
                for (int i = 0; i < count && i < ids.size(); i++) {
                    int j = random.nextInt(ids.size());
                    Collections.swap(ids, i, j);
                    sampledIds.add(ids.get(i));
                }

                List<NewsItem> syncList =
                        Collections.synchronizedList(new ArrayList<>());
                CountDownLatch latch = new CountDownLatch(count);
                ExecutorService executor = Executors.newFixedThreadPool(count);

                postStatus("Loading " + sampledIds.size() + " stories in parallel...");

                for (int i = 0; i < count; i++) {
                    final int newsId = sampledIds.get(i);
                    executor.submit(() -> {
                        try {
                            String itemUrl = ITEM_URL + newsId + ".json";
                            Request itemRequest = new Request.Builder()
                                    .url(itemUrl)
                                    .build();
                            Response itemResponse =
                                    okHttpClient.newCall(itemRequest).execute();
                            String itemJson = itemResponse.body().string();
                            NewsItem news = gson.fromJson(itemJson, NewsItem.class);
                            if (news != null && news.getTitle() != null) {
                                syncList.add(news);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            latch.countDown();
                        }
                    });
                }

                latch.await();
                executor.shutdown();
                newsList.addAll(syncList);

            } catch (IOException e) {
                e.printStackTrace();
                errorMessage = "Network error: " + e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = "Load failed: " + e.getMessage();
            }

            List<NewsItem> finalNewsList = newsList;
            String finalError = errorMessage;

            runOnUiThread(() -> {
                setLoading(false);
                if (finalError != null) {
                    tvStatus.setText(finalError);
                } else {
                    tvCount.setText(String.valueOf(finalNewsList.size()));
                    tvStatus.setText("Updated just now");
                    newsAdapter.updateData(finalNewsList);
                }
            });
        }).start();
    }

    private void postStatus(final String text) {
        runOnUiThread(() -> tvStatus.setText(text));
    }
}
