package com.example.s236307.quotify.network;

/**
 * Created by mkluf on 11/25/2016.
 */

public abstract class CustomRunnable<T> implements Runnable {
    public T data;
    public String url;

    @Override
    abstract public void run();
}
