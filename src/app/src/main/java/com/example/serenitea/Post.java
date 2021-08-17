package com.example.serenitea;

public class Post {
    private String user_id, name, avatar, date, content;
    private int color, background, font;

    public Post(String user_id, String name, String avatar, String date, String content, int color, int background, int font) {
        this.user_id = user_id;
        this.name = name;
        this.avatar = avatar;
        this.date = date;
        this.content = content;
        this.color = color;
        this.background = background;
        this.font = font;
    }

    public String getUserId() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public int getColor() {
        return color;
    }

    public int getBackground() {
        return background;
    }

    public int getFont() {
        return font;
    }
}
