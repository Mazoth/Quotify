package com.example.s236307.quotify.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> tClass;
    private Map<String, String> headers;
    private final Response.Listener<T> listener;

    public GsonRequest(String url, Class<T> tClass, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.tClass = tClass;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            return Response.success(gson.fromJson(json, tClass),
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e){
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T t) {
        listener.onResponse(t);
    }
}
