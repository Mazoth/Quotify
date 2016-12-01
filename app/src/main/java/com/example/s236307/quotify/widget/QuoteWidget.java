package com.example.s236307.quotify.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.example.s236307.quotify.service.QuoteService;

import java.util.Calendar;

/**
 * Created by Marius on 01.12.2016.
 */

public class QuoteWidget extends AppWidgetProvider {
    public static String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
    private PendingIntent service = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId : appWidgetIds) {
            final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            final Calendar TIME = Calendar.getInstance();
            TIME.set(Calendar.MINUTE, 0);
            TIME.set(Calendar.SECOND, 0);
            TIME.set(Calendar.MILLISECOND, 0);

            final Intent i = new Intent(context, QuoteService.class);

            if (service == null) service =
                    PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);

            m.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, service);
            super.onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(service != null) m.cancel(service);
    }
}
