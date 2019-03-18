package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 140;

    private EditText etCompose;
    private Button btnTweet;
    private TextView tvTweetChar;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        tvTweetChar = findViewById(R.id.tvTweetChar);

        //Set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                //TODO: Error-Handling
                if(tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Your tweet is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweetContent.length()>MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Your tweet is too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this, tweetContent ,Toast.LENGTH_SHORT).show();
                //Make API call to Twitter to publish the content in edit text
                client.composeTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("TwitterClient", "Successfully posted tweet! " + response.toString());
                        try {
                            Tweet tweet = Tweet.fromJson(response);
                            Intent data = new Intent();
                            data.putExtra("tweet", Parcels.wrap(tweet));
                            //set result code and bundle data for response
                            setResult(RESULT_OK, data);
                            //closes the activity, pass data to parent
                            finish();
                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("TwitterClient", "Failed to post tweet: " + responseString);
                    }
                });
            }
        });

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int before) {
                tvTweetChar.setText(String.valueOf(s.length()) + "/140");
                if (s.length()>MAX_TWEET_LENGTH) {
                    tvTweetChar.setTextColor(Color.RED);
                }
                if (s.length()<=MAX_TWEET_LENGTH) {
                    tvTweetChar.setTextColor(Color.BLACK);
                }
                if (s.length()==0) {
                    tvTweetChar.setText("/140");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}
