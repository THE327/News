package com.example.test.model;

import com.google.gson.annotations.SerializedName;

/**
 * ============================================================
 *  新闻实体类 —— 第4课更新
 * ============================================================
 *
 *  第2节课：定义了字段和构造方法
 *  第3节课：没有任何改动（手动 JSONObject 取值）
 *  第4节课：添加 Gson 的 @SerializedName 注解
 *
 *
 *  ═══════════════════════════════════════════════════════════
 *  为什么要加 @SerializedName？
 *  ═══════════════════════════════════════════════════════════
 *
 *  Gson 的工作原理：
 *    JSON 的 "key"  ←→  Java 的 "成员变量名"
 *    自动匹配，自动赋值
 *
 *  大部分情况下，JSON 的 key 和 Java 变量名是一样的，不需要注解
 *  例如：JSON "title" → Java String title → 自动匹配 ✓
 *
 *  但有些情况下名字不一样，就需要 @SerializedName 告诉 Gson 对应关系：
 *    JSON "by" → Java String by → 名字一样，本不需要注解
 *    但 "by" 是 Java 关键字/常见缩写，加上注解可以让代码更清晰
 *
 *  本节课的实际用法：
 *    NewsItem news = gson.fromJson(jsonString, NewsItem.class);
 *    这一行代码 = 上节课 8 行手动取值的活
 *
 *
 *  ═══════════════════════════════════════════════════════════
 *  Hacker News 单条新闻 JSON 结构（回顾）
 *  ═══════════════════════════════════════════════════════════
 *
 *  {
 *      "id": 37590397,
 *      "type": "story",
 *      "by": "todsacerdoti",
 *      "time": 1712723456,
 *      "title": "Show HN: I built a tool to visualize Git history",
 *      "url": "https://github.com/example/git-viz",
 *      "score": 342,
 *      "descendants": 128
 *  }
 */
public class NewsItem {

    // ── 成员变量（对应 JSON 字段）─────────────────────────────
    // @SerializedName 告诉 Gson："JSON 里叫这个名字的字段，赋值给这个变量"
    // 如果变量名和 JSON key 完全一致，@SerializedName 可以省略
    // 这里为了教学清晰，全部显式标注

    @SerializedName("id")
    private int id;              // 新闻编号

    @SerializedName("type")
    private String type;         // 类型（story / ask / job）

    @SerializedName("by")
    private String by;           // 作者

    @SerializedName("time")
    private long time;           // 发布时间（Unix 时间戳，秒）

    @SerializedName("title")
    private String title;        // 标题

    @SerializedName("url")
    private String url;          // 原文链接（可能为空）

    @SerializedName("score")
    private int score;           // 点赞分数

    @SerializedName("descendants")
    private int descendants;     // 评论数量


    // ── 构造方法 ─────────────────────────────────────────────
    //  Gson 需要一个无参构造方法来创建对象，然后再通过反射赋值
    //  如果你写了有参构造方法，必须显式写一个无参的，否则 Gson 会报错
    public NewsItem() {
        // 空的，Gson 通过反射给字段赋值
    }

    // 有参构造方法（保留，方便手动创建对象时使用）
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

    // ── Getter 方法 ──────────────────────────────────────────
    public int    getId()          { return id; }
    public String getType()        { return type; }
    public String getBy()          { return by; }
    public long   getTime()        { return time; }
    public String getTitle()       { return title; }
    public String getUrl()         { return url; }
    public int    getScore()       { return score; }
    public int    getDescendants() { return descendants; }
}
