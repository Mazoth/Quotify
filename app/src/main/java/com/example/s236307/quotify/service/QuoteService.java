package com.example.s236307.quotify.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.s236307.quotify.R;
import com.example.s236307.quotify.network.JSONUtils;
import com.example.s236307.quotify.network.OnRequestCompleteListener;
import com.example.s236307.quotify.objects.Quote;
import com.example.s236307.quotify.widget.QuoteWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class QuoteService extends Service {
    private RemoteViews view;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String category = intent.getExtras().getString("category", null);
        boolean randomQuote = intent.getExtras().getBoolean("randomQuote", false);
        int appWidgetId = intent.getExtras().getInt("appWidgetId", 0);
        getNewQuote(category, randomQuote, appWidgetId);
        stopSelf(startId);
        return super.onStartCommand(intent, flags, startId);
    }

    private void getNewQuote(String category, boolean randomQuote, final int appWidgetId) {
        OnRequestCompleteListener listener = new OnRequestCompleteListener() {
            @Override
            public void onRequestCompleted(JSONObject response, boolean isPathForQuoteOfTheDay) throws JSONException {
                Quote quote = JSONUtils.fromJSONToQuote(response, isPathForQuoteOfTheDay);
                QuoteWidget.bindRemoteViews(getBaseContext(), quote, appWidgetId);
            }
        };
        String path = JSONUtils.getPath(category, randomQuote);
        Log.i("QuoteService with ID: ", appWidgetId + "" + "with path " + path);
        JSONUtils.fetchQuoteAsJson(this, path, listener);
    }

}
