package com.example.s236307.quotify.widget;

import android.app.Activity;
import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.s236307.quotify.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Marius on 01.12.2016.
 */

public class QuoteWidgetConfig extends Activity {
    int thisWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    public static String ACTION_WIDGET_CONFIGRE = "WIDGET_CONFIGURED";
    private static final String PREFS_NAME = "com.example.s236307.quotify.widget.QuoteWidget";
    private static final String PREF_CATEGORY_KEY = "category_";
    private static final String PREF_RANDOM_KEY = "random_";
    CheckBox wantRandomQuotes;
    Spinner categorySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.quote_widget_config_activity);

        wantRandomQuotes = (CheckBox) findViewById(R.id.random_or_qod_checkbox);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null)
            thisWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (thisWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) finish();

        //Set default values, if this widget already has been configured
        categorySpinner.setSelection(getSelectedPosition(loadCategoryPref(this, thisWidgetId)));
        wantRandomQuotes.setChecked(loadRandomQuotePref(this, thisWidgetId));
        findViewById(R.id.add_widget_button).setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Context context = getBaseContext();
            String category = categorySpinner.getSelectedItem().toString().toLowerCase();
            boolean wantRandomQuote = wantRandomQuotes.isChecked();
            saveCheckBoxResultPref(context, thisWidgetId, wantRandomQuote);
            saveCategoryPref(context, thisWidgetId, category);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, thisWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    private void saveCategoryPref(Context context, int thisWidgetId, String category) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_CATEGORY_KEY + thisWidgetId, category);
        prefs.apply();
    }

    private void saveCheckBoxResultPref(Context context, int thisWidgetId, boolean wantRandomQuote) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putBoolean(PREF_RANDOM_KEY + thisWidgetId, wantRandomQuote);
        prefs.apply();
    }

    /**
     * @param context      This is the context of the widget based on ID
     * @param thisWidgetId This is to differentiate widgets, incase there are more than 1
     * @return Either the category or null, if it isn't specified
     */
    public static String loadCategoryPref(Context context, int thisWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREF_CATEGORY_KEY + thisWidgetId, null);
    }

    /**
     * @param context      Same as above
     * @param thisWidgetId Same as above
     * @return I return a boolean this time.
     */
    public static boolean loadRandomQuotePref(Context context, int thisWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(PREF_RANDOM_KEY + thisWidgetId, false);
    }

    /**
     * @param category If the category is saved in the preferences, I'd wish to find the position it's at
     * @return the position of the selected category, if it isn't null
     */
    private int getSelectedPosition(String category) {
        String[] categories = getResources().getStringArray(R.array.categories);
        int position = 0;
        for (String s : categories) {
            position++;
            if (s.equals(category)) return position;
        }
        return 0;
    }
}
