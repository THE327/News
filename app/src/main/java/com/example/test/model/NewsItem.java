package com.example.test.model;

import com.google.gson.annotations.SerializedName;

/**
 * Hacker News story item.
 */
public class NewsItem {

    @SerializedName("id")
    private int id;

    @SerializedName("type")
    private String type;

    @SerializedName("by")
    private String by;

    @SerializedName("time")
    private long time;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("score")
    private int score;

    @SerializedName("descendants")
    private int descendants;

    public NewsItem() {
    }

    public NewsItem(int id, String type, String by, long time,
                    String title, String url, int score, int descendants) {
        this.id          = id;
        this.type        = type;
        this.by          = by;
        this.time        = time;
        this.title       = title;
        this.url         = url;
        this.score       = score;
        this.descendants = descendants;
    }

    public int    getId()          { return id; }
    public String getType()        { return type; }
    public String getBy()          { return by; }
    public long   getTime()        { return time; }
    public String getTitle()       { return title; }
    public String getUrl()         { return url; }
    public int    getScore()       { return score; }
    public int    getDescendants() { return descendants; }
}
