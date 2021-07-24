package com.example.serenitea;

public class Notification {
    private String date, from, quote, time;

    public Notification(){

    }

    public Notification(String date, String from, String quote, String time) {
        this.date = date;
        this.from = from;
        this.quote = quote;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
