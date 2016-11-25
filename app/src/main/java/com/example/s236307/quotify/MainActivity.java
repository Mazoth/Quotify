package com.example.s236307.quotify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s236307.quotify.network.GetJSON;
import com.example.s236307.quotify.objects.Quote;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textField = (TextView) findViewById(R.id.quote_text);
        TextView author = (TextView) findViewById(R.id.author_text);
        Quote q = GetJSON.fetchQuote("quote.json", this);
        if (q != null) {
            textField.setText(q.getQuote());
            author.setText(q.getAuthor());
            Toast.makeText(this, q.toString(), Toast.LENGTH_SHORT).show();
        } else
            textField.setText("Network error");
    }
}
