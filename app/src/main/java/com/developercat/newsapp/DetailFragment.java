package com.developercat.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment {
    private static final String ID = "id";

    private int newsId;

    ImageView newsImage;
    TextView newsTitle, newsSubTitle, newsImageDescription, newsTime, newsPublisher, newsText;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(int id) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsId = getArguments().getInt(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        newsImage = view.findViewById(R.id.newsImage);
        newsTitle = view.findViewById(R.id.newsTitle);
        newsSubTitle = view.findViewById(R.id.newsSubTitle);
        newsImageDescription = view.findViewById(R.id.newsImageDescription);
        newsTime = view.findViewById(R.id.newsTime);
        newsPublisher = view.findViewById(R.id.newsPublisher);
        newsText = view.findViewById(R.id.newsText);

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        NewsItem item = databaseHelper.getNewsItem(newsId);

        Picasso.get().load(item.getImage()).into(newsImage);
        newsImageDescription.setText(item.getImageDescription());
        newsTitle.setText(item.getTitle());
        newsSubTitle.setText(item.getSubTitle());
        newsTime.setText(item.getTime());
        newsPublisher.setText(item.getPublisher());
        newsText.setText(item.getText());

        return view;
    }

    public int getShownId() {
        return newsId;
    }
}
