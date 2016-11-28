package com.example.s236307.quotify.network;

import android.content.Context;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class JSONUtils {
    private static final String API_KEY = "DWuym0C0qOV8EcyNhQFNIAeF";
    private static int MY_SOCKET_TIMOUT_MS = 10000;

    public static void fetchQuoteAsJson(final Context context, final String path, final OnRequestCompleteListener listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //I have this check to customize the respons, based on what I'm fetching from the API
        final boolean isPathForQuoteOfTheDay = path.toLowerCase().contains("qod.json");
        String ENDPOINT = "http://quotes.rest/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ENDPOINT + path,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listener.onRequestCompleted(response, isPathForQuoteOfTheDay);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    listener.onRequestCompleted(new JSONObject(error.toString()), isPathForQuoteOfTheDay);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-TheySaidSo-Api-Secret", API_KEY);
                return headers;
            }
        };
        jsonObjectRequest.setShouldCache(false);

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * This class is used to convert standard JSONObject response from the API and convert it into
     * a Quote object which is used elsewhere.
     *
     * @param jsonObject JSONObject that is fetched from the server
     * @param isPathForQuoteOfTheDay See parent
     * @return A new Quote object
     * @throws JSONException
     */
    public static Quote fromJSONToQuote(JSONObject jsonObject, boolean isPathForQuoteOfTheDay) throws JSONException {
        JSONObject mJsonObject = jsonObject.getJSONObject("contents");
        if(isPathForQuoteOfTheDay) {
            mJsonObject = jsonObject.getJSONArray("quotes").getJSONObject(0);
        }
        String quoteString = mJsonObject.getString("quote");
        String authorString = mJsonObject.getString("author");
        return new Quote(quoteString, authorString);
    }
}
