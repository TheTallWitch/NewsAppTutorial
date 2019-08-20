package com.developercat.newsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Intent intent = getIntent();
        if (intent != null) {
            int id = intent.getIntExtra("id", 0);
            DetailFragment detailFragment = DetailFragment.newInstance(id);
            getSupportFragmentManager().beginTransaction().add(R.id.detailHolder, detailFragment).commit();
        }
    }
}
