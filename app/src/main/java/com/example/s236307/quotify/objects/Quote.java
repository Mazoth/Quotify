package com.example.s236307.quotify.objects;

/**
 * Object Quote.
 */

public class Quote {
    private String author;
    private String quote;

    public Quote(String quote, String author) {
        this.quote = quote;
        this.author = author;
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
