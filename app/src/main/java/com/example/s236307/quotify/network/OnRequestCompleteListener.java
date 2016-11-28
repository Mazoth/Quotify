package com.example.s236307.quotify.network;

import org.json.JSONException;
import org.json.JSONObject;

public interface OnRequestCompleteListener {
    void onRequestCompleted(JSONObject response, boolean isPathForQuoteOfTheDay) throws JSONException;
}
