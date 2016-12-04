package com.example.s236307.quotify.objects;

/**
 *  Simple Quote Object.
 */

public class Quote {
    private String author;
    private String quote;

    public Quote(String quote, String author) {
        this.author = author.equals("null") ? "- Unknown" : "- " + author;
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public String getQuote() {
        return quote;
    }

    @Override
    public String toString() {
        return author + " - " + quote;
    }
}
