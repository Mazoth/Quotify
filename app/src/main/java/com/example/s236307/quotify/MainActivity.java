package com.example.s236307.quotify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s236307.quotify.network.JSONUtils;
import com.example.s236307.quotify.network.OnRequestCompleteListener;
import com.example.s236307.quotify.objects.Quote;

import org.json.JSONException;
import org.json.JSONObject;

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
    }

    public void getQuote(View view){
//        listener = new OnRequestCompleteListener() {
//            @Override
//            public void onRequestCompleted(JSONObject response) throws JSONException {
//                textField.setText(response.getJSONObject("contents").getJSONArray("quotes").getJSONObject(0).getString("quote"));
//                author.setText(response.getJSONObject("contents").getJSONArray("quotes").getJSONObject(0).getString("author"));
//            }
//        };
        JSONUtils.fetchQuoteAsJson(this, "quote.json", listener);
        Toast.makeText(this, "PresseD", Toast.LENGTH_SHORT).show();
    }
}
