package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.sql.Time;

@Parcel
public class Tweet {
    public String body;
    public long uid;
    public String createdAt;
    public User user;

    //needed for parcel
    public Tweet() {

    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }

    public String getFormattedTimeStamp() {
        return TimeFormatter.getTimeDifference(createdAt);
    }
}
