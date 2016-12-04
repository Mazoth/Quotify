package com.example.s236307.quotify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s236307.quotify.network.JSONUtils;
import com.example.s236307.quotify.network.OnRequestCompleteListener;
import com.example.s236307.quotify.objects.Quote;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * This class is mandetory for my application, as I can't seem to be able to run widgets, update
 * them and make them work properly, without this main activity at the root. This activity simply
 * tells the user that the real "magic" happens with the widget, and I showcase how the widget looks
 * like visually.
 */
public class MainActivity extends AppCompatActivity {
    OnRequestCompleteListener listener;
    TextView textField, author;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textField = (TextView) findViewById(R.id.quote_text);
        author = (TextView) findViewById(R.id.author_text);
        textField.setText(R.string.default_quote);
        author.setText(R.string.default_author);
        listener = new OnRequestCompleteListener() {
            @Override
            public void onRequestCompleted(JSONObject response, boolean isPathForQuoteOfTheDay) throws JSONException {
                Quote quote = JSONUtils.fromJSONToQuote(response, isPathForQuoteOfTheDay);
                textField.setText(quote.getQuote());
                author.setText(quote.getAuthor());
            }
        };
        JSONUtils.fetchQuoteAsJson(this, "qod.json?category=funny", listener);
    }
}
