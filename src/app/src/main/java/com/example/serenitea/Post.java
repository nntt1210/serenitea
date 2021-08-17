package com.example.serenitea;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Post  implements Comparable<Post> {
    private String userId, date, content;
    String postId;
    private int color, background, font, size;

    public Post() {
    }

    public Post(String userId, String date, String content, int color, int background, int font, int size) {
        this.userId = userId;
        this.date = date;
        this.content = content;
        this.color = color;
        this.background = background;
        this.font = font;
        this.size = size;
    }

    public String getUserId() {
        return userId;
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

    public int getSize() {
        return size;
    }

    @Override
    public int compareTo(Post o) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {
            return format.parse(o.getDate()).compareTo(format.parse(this.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
