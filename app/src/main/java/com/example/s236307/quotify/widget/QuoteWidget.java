package com.example.s236307.quotify.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.s236307.quotify.service.QuoteService;

import java.util.Calendar;

/**
 * Created by Marius on 01.12.2016.
 */

public class QuoteWidget extends AppWidgetProvider {
    public static String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
    private static PendingIntent service = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            String category = QuoteWidgetConfig.loadCategoryPref(context, appWidgetId);
            boolean randomQuote = QuoteWidgetConfig.loadRandomQuotePref(context, appWidgetId);
            updateAppWidget(context, appWidgetId, category, randomQuote);
//            super.onUpdate(context,appWidgetManager,appWidgetIds);
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (service != null) m.cancel(service);
    }

    public static void updateAppWidget(Context context, int appWidgetId,
                                       String category, boolean randomQuote) {
        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final Calendar TIME = Calendar.getInstance();
        TIME.set(Calendar.MINUTE, 0);
        TIME.set(Calendar.SECOND, 0);
        TIME.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(context, QuoteService.class);
        intent.putExtra("category", category);
        intent.putExtra("randomQuote", randomQuote);
        intent.putExtra("appWidgetId", appWidgetId);
        if (service == null) service =
                PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        m.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(), 10000, service);
    }
}
