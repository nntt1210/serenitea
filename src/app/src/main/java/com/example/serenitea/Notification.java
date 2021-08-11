package com.example.serenitea;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Notification implements Comparable<Notification> {
    private String date, from, quote, status, key;

    public Notification() {

    }

    public Notification(String key) {
        this.key = key;
    }

    public Notification(String date, String from, String quote, String status) {
        this.date = date;
        this.from = from;
        this.quote = quote;
        this.status = status;
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

    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int compareTo(Notification o) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {
            return format.parse(o.getDate()).compareTo(format.parse(this.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
