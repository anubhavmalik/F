package com.teamrevelador.f;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main2Activity extends AppCompatActivity {

    ArrayList<String> arrayList;
    Button button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();

        button = findViewById(R.id.show_feed_button);

        TextView nameTextView = findViewById(R.id.name_text_view);
        nameTextView.setText(intent.getStringExtra("name")+ " " + intent.getStringExtra("surname"));
        TextView ageTextView = findViewById(R.id.age_text_view);
        ageTextView.setText(intent.getStringExtra("imageUrl"));
        CircleImageView circleImageView = findViewById(R.id.circle_image_view);

        Glide.with(this)
                .asBitmap()
                .load(intent.getStringExtra("imageUrl"))
                .into(circleImageView);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this,FeedActivity.class));
            }
        });
//        intent.putExtra("name",firstName);
//        intent.putExtra("surname",lastName);
//        intent.putExtra("imageUrl",profilePicture.toString());
    }
}
