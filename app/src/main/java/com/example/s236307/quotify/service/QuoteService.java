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
        getNewQuote();
        return super.onStartCommand(intent, flags, startId);
    }

    private void getNewQuote() {
        String lastUpdated = DateFormat.format("hh:mm:ss", new Date()).toString();
        OnRequestCompleteListener listener = new OnRequestCompleteListener() {
            @Override
            public void onRequestCompleted(JSONObject response, boolean isPathForQuoteOfTheDay) throws JSONException {
                Quote quote = JSONUtils.fromJSONToQuote(response, isPathForQuoteOfTheDay);
                view = new RemoteViews(getPackageName(), R.layout.quote_widget_layout);
                view.setTextViewText(R.id.widget_quote_text, quote.getQuote());
                view.setTextViewText(R.id.widget_author_text, quote.getAuthor());
                ComponentName thisWidget = new ComponentName(getBaseContext(), QuoteWidget.class);
                AppWidgetManager manager = AppWidgetManager.getInstance(getBaseContext());
                manager.updateAppWidget(thisWidget, view);
            }
        };
        JSONUtils.fetchQuoteAsJson(this, "quote.json", listener);

    }
}
