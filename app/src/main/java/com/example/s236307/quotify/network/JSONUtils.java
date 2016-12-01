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
 * JSONUtils is a class I created to be able to reuse the method I created intended for communicating
 * with the API through Volley requests.
 */
public class JSONUtils {
    private static final String API_KEY = "DWuym0C0qOV8EcyNhQFNIAeF";
    private static final String ENDPOINT = "http://quotes.rest/";

    /**
     * This method is used to fetch a quote from the API as a JSONObject, which is later used to
     * extract the quote and the author of the quote. When the Volley request is finished, the listener
     * that is parsed through does it job.
     *
     * @param context is the context from which this method is called from
     * @param path is the custom path that is parsed through depending on what type of quote is wanted
     * @param listener a listener that listens for changes and updates the widget/app when the job is finished.
     */
    public static void fetchQuoteAsJson(final Context context, final String path, final OnRequestCompleteListener listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final boolean isPathForQuoteOfTheDay = path.toLowerCase().contains("qod.json");

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

        //I had a bug where I wouldn't get a new quote from the API, but rather the latest one
        //which is stored in the cache. Therefore I decided to disable this feature alltogether.
        jsonObjectRequest.setShouldCache(false);

        //  I need to set a retry policy, because the API is slower to respond than Volley allows.
        int MY_SOCKET_TIMOUT_MS = 1000000;
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        //Add the request to the queue.
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * This class is used to convert standard JSONObject response from the API and convert it into
     * a Quote object which is used elsewhere. In the case of the JSON that was requested from the
     * API was a "Quote of the day" I have a boolean check that ascertains this. The reason for this
     * is because Theysaidso.com have decided to nest their quote of the day inside an array,
     * as opposed to having it as a single JSON Object like they have for the random quote service.
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
