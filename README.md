# Android News App

## 项目简介
本项目是一个基于 Java 的 Android 新闻阅读 App，通过 Hacker News API 获取热门新闻列表，支持浏览、刷新和跳转原文。

## 主要功能
1. 从 Hacker News API 获取热门新闻列表
2. 并行网络请求，随机采样提升加载速度
3. 下拉刷新更新新闻内容
4. 点击新闻跳转浏览器查看原文
5. 空状态与加载状态提示
6. 显示新闻分数、作者、时间、评论数和来源域名

## 使用技术
1. Java
2. Android Studio
3. XML Layout（Material Design 风格）
4. RecyclerView + SwipeRefreshLayout
5. OkHttp（网络请求）
6. Gson（JSON 解析）
7. Glide（图片加载）
8. Git

## 项目结构
MainActivity.java：主页面，负责网络请求与用户交互
NewsItem.java：新闻数据实体类
NewsAdapter.java：新闻列表适配器

## 运行方式
1. 使用 Android Studio 打开项目；
2. 等待 Gradle 同步完成；
3. 连接模拟器或真实设备；
4. 点击 Run 运行项目。

## 开发过程
本项目使用 Git 进行版本管理，主要提交记录包括：
1. 创建 Android 项目；
2. 完成主页面布局；
3. 清理冗余注释并修复主题引用；
4. 添加下拉刷新与点击跳转浏览器功能；
5. 添加空状态视图并改进卡片布局；
6. 修复运行时崩溃问题；
7. 完善 README。
