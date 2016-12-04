package com.example.s236307.quotify.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.s236307.quotify.R;
import com.example.s236307.quotify.objects.Quote;
import com.example.s236307.quotify.service.QuoteService;

import java.util.Calendar;


/**
 * This is the Widget provider that is created whenever a user wishes to read savvy quotes on their
 * home screen. This application allows for multiple instances of widgets updating at the same time.
 */
public class QuoteWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Toast.makeText(context, "The widget was onUpdated()!", Toast.LENGTH_SHORT).show();
        for (int appWidgetId : appWidgetIds) {
            //Check if the widget is configured yet
            if (QuoteWidgetConfig.loadConfigedPref(context, appWidgetId)) {
                String category = QuoteWidgetConfig.loadCategoryPref(context, appWidgetId);
                boolean randomQuote = QuoteWidgetConfig.loadRandomQuotePref(context, appWidgetId);
                int interval = QuoteWidgetConfig.loadIntervalPref(context, appWidgetId);
                Log.i("QuoteWidget", category + randomQuote);
                updateAppWidget(context, appWidgetId, category, randomQuote, interval);
            } else {
                //If not, I request a random quote for the first time the widget runs it's service.
                //Once the widget is updated, it runs as requested in config.
                updateAppWidget(context, appWidgetId, "", true, 24);
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * This method uses an Alarmmanager to individually start a new service for each Widget
     * that updates every so often as set in the config.
     *
     * @param context     Context of the activity
     * @param appWidgetId Unique identifier for widget that wishes to be updated
     * @param category    Category we'd like to request from the API
     * @param randomQuote A boolean indicating if the user wishes for a random quote or the quote of the day
     */
    public void updateAppWidget(Context context, int appWidgetId,
                                String category, boolean randomQuote, int interval) {
        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar TIME = Calendar.getInstance();
        TIME.set(Calendar.MINUTE, 0);
        TIME.set(Calendar.SECOND, 0);
        TIME.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(context, QuoteService.class);
        intent.putExtra("category", category);
        intent.putExtra("randomQuote", randomQuote);
        intent.putExtra("appWidgetId", appWidgetId);
        intent.setData(Uri.parse("mywidgets://" + appWidgetId));
        PendingIntent service = PendingIntent.getService(context, appWidgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //If the teacher wishes to test the frequency of the test, this is the number to override. It can be
        // as low as 1000ms, but this is not recommended.
        interval = interval * 3600000;

        m.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(), interval, service);
    }

    public static void bindRemoteViews(Context context, Quote quote, int appWidgetId) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.quote_widget_layout);
        view.setTextViewText(R.id.widget_quote_text, quote.getQuote());
        view.setTextViewText(R.id.widget_author_text, quote.getAuthor());
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        Intent configIntent = new Intent(context, QuoteWidgetConfig.class);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        configIntent.setData(Uri.withAppendedPath(Uri.parse("mywidget://id/"), String.valueOf(appWidgetId)));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.config_button, pendingIntent);

        manager.updateAppWidget(appWidgetId, view);
    }
}
