package com.teamrevelador.f;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        final ListView listView = findViewById(R.id.friends_list);

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Log.d("RESPONSE ", response.getRawResponse() + "");
                        try {
                            JSONObject jsonObject = new JSONObject(response.getRawResponse());
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                arrayList.add(jsonArray.getJSONObject(i).getString("story"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

  /* handle the result */
                        ArrayAdapter arrayAdapter = new ArrayAdapter(FeedActivity.this, android.R.layout.simple_list_item_1, arrayList);
                        listView.setAdapter(arrayAdapter);
                    }
                }
        ).executeAsync();


    }
}
