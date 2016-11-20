package com.example.s236307.quotify;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.InputDeviceCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s236307.quotify.network.GsonRequest;
import com.example.s236307.quotify.objects.Quote;
import com.example.s236307.quotify.objects.Quotes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String ENDPOINT = "http://quotes.rest/qod.json";
    private static final String API_KEY = "DWuym0C0qOV8EcyNhQFNIAeF";
    private RequestQueue requestQueue;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        fetchQuote();


    }

    private void fetchQuote() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-TheySaidSo-Api-Secret", API_KEY);

        @SuppressWarnings("unchecked")
        GsonRequest gsonRequest = new GsonRequest(ENDPOINT, Quotes.class, headers, onQuoteLoaded, onQuoteError);

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ENDPOINT, new JSONObject(headers), onQuoteLoaded, onQuoteError);
//        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onQuoteLoaded, onQuoteError) {
//            @Override
//            public Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("X-TheySaidSo-Api-Secret", API_KEY);
//                return params;
//            }
//        };
        requestQueue.add(gsonRequest);
    }

    private final Response.Listener<Quotes> onQuoteLoaded = new Response.Listener<Quotes>() {
        @Override
        public void onResponse(Quotes quotes) {
            for(int i = 0; i < quotes.getQuotes().size(); i++) {
                Quote q = quotes.getQuotes().get(i);
                Log.i("MainActivity", q.toString());
            }
        }
    };

    private final Response.ErrorListener onQuoteError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("MainActivity", volleyError.toString());
        }
    };
}
