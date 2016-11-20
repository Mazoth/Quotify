package com.example.s236307.quotify.objects;

/**
 * Object Quote.
 */

public class Quote {
    private String author;
    private String quote;

    @Override
    public String toString() {
        return author + " - " + quote;
    }
}
