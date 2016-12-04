package com.example.s236307.quotify.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.s236307.quotify.R;

/**
 */

public class QuoteWidgetConfig extends Activity {
    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private static final String PREFS_NAME = "com.example.s236307.quotify.widget.QuoteWidget";
    private static final String PREF_CATEGORY_KEY = "category_";
    private static final String PREF_RANDOM_KEY = "random_";
    private static final String PREF_BOOLEAN_EDITED = "wasedited_";
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
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) finish();

        //Set default values, if this widget already has been configured
        Toast.makeText(this, loadCategoryPref(this, appWidgetId), Toast.LENGTH_SHORT).show();
        categorySpinner.setSelection(getSelectedPosition(loadCategoryPref(this, appWidgetId)));
        wantRandomQuotes.setChecked(loadRandomQuotePref(this, appWidgetId));
        findViewById(R.id.add_widget_button).setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Context context = getBaseContext();

            String category = categorySpinner.getSelectedItem().toString().toLowerCase();
            boolean wantRandomQuote = wantRandomQuotes.isChecked();

            saveCheckBoxResultPref(context, appWidgetId, wantRandomQuote);
            saveCategoryPref(context, appWidgetId, category);
            saveEditedBooleanPref(context, appWidgetId, true);

            Intent firstUpdate = new Intent();
            firstUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, firstUpdate);

            finish();
        }
    };

    private void saveCategoryPref(Context context, int appWidgetId, String category) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_CATEGORY_KEY + appWidgetId, category);
        prefs.apply();
    }

    private void saveCheckBoxResultPref(Context context, int appWidgetId, boolean wantRandomQuote) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putBoolean(PREF_RANDOM_KEY + appWidgetId, wantRandomQuote);
        prefs.apply();
    }

    private void saveEditedBooleanPref(Context context, int appWidgetId, boolean configured) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putBoolean(PREF_BOOLEAN_EDITED + appWidgetId, configured);
        prefs.apply();
    }

    /**
     * @param context     This is the context of the widget based on ID
     * @param appWidgetId This is to differentiate widgets, incase there are more than 1
     * @return Either the category or null, if it isn't specified
     */
    public static String loadCategoryPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREF_CATEGORY_KEY + appWidgetId, null);
    }

    /**
     * @param context     Same as above
     * @param appWidgetId Same as above
     * @return I return a boolean this time.
     */
    public static boolean loadRandomQuotePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(PREF_RANDOM_KEY + appWidgetId, false);
    }

    public static boolean loadConfigedPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(PREF_BOOLEAN_EDITED + appWidgetId, false);
    }

    /**
     * @param category If the category is saved in the preferences, I'd wish to find the position it's at
     * @return the position of the selected category, if it isn't null
     */
    private int getSelectedPosition(String category) {
        String[] categories = getResources().getStringArray(R.array.categories);
        int position = 0;
        for (String s : categories) {
            if (s.toLowerCase().equals(category)) return position;
            position++;
        }
        return 0;
    }
}
