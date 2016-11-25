package com.example.s236307.quotify.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.s236307.quotify.objects.Quote;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A class
 */

public class GetJSON {
    private static final String API_KEY = "DWuym0C0qOV8EcyNhQFNIAeF";
    private static Quote quote;
    private static String ENDPOINT = "http://quotes.rest/";
    private static int MY_SOCKET_TIMOUT_MS = 5000;

    public static Quote fetchQuote(String FORMAT, Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ENDPOINT + FORMAT,
                new JSONObject(), onQuoteLoaded, onQuoteError){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-TheySaidSo-Api-Secret", API_KEY);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        requestQueue.add(jsonObjectRequest);
        return quote;
    }

    private static final Response.Listener<JSONObject> onQuoteLoaded = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            JSONObject quoteAsJsonObject;
            try {
                quoteAsJsonObject = jsonObject.getJSONObject("contents");
                String qu = quoteAsJsonObject.getString("quote");
                String au = quoteAsJsonObject.getString("author");
                quote = new Quote(qu, au);
                Log.i("MainActivity", quote.toString());
                Log.i("MainActivity", jsonObject.getJSONObject("contents").getString("quote"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private static final Response.ErrorListener onQuoteError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("MainActivity", volleyError.toString());
        }
    };
}
