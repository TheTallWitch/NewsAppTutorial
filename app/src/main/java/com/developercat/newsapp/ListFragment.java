package com.developercat.newsapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    boolean isLandscape = false;
    int currentNewsId = 0;

    RecyclerView newsRecyclerView;
    DatabaseHelper databaseHelper;
    NewsAdapter newsAdapter;

    List<NewsItem> newsItems = new ArrayList<>();

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);

        databaseHelper = new DatabaseHelper(getContext());

        newsItems = databaseHelper.getAllNews();
        if (newsItems.size() == 0) {
            HttpGetRequest httpGetRequest = new HttpGetRequest(getContext(), new HttpGetRequest.ResultListener() {
                @Override
                public void onResult(String result) {
                    PopulateList(result);
                }
            });
            httpGetRequest.execute("http://www.hade3.com/_assets/news.aspx");
        }
        else {
            MakeList();
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandscape = true;
        }

        if (isLandscape) {
            if (currentNewsId == 0) {
                currentNewsId = databaseHelper.getFirstNewsId();
            }

            ShowDetails(currentNewsId, 0);
        }

        return view;
    }

    public void ShowDetails(int id, int position) {
        currentNewsId = id;

        if (isLandscape) {
            DetailFragment detailFragment = (DetailFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.detailContainer);
            if (detailFragment == null || currentNewsId != detailFragment.getShownId()) {
                detailFragment = DetailFragment.newInstance(currentNewsId);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.detailContainer, detailFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();

                newsAdapter.SetSelected(position);
            }
        }
        else {
            Intent intent = new Intent(getContext(), NewsDetailActivity.class);
            intent.putExtra("id", currentNewsId);
            startActivity(intent);
        }
    }

    public void PopulateList(String result) {
        try {
            JSONArray objectList = new JSONArray(result);
            for (int i = 0; i < objectList.length(); i++) {
                JSONObject item = objectList.getJSONObject(i);
                newsItems.add(new NewsItem(item.getInt("id"), item.getString("title"), item.getString("subTitle"), item.getString("text"), item.getString("image"), item.getString("imageDesc"), item.getString("time"), item.getString("publisher")));
                databaseHelper.insertNews(item.getInt("id"), item.getString("title"), item.getString("subTitle"), item.getString("text"), item.getString("image"), item.getString("imageDesc"), item.getString("time"), item.getString("publisher"));
            }

            MakeList();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void MakeList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        newsRecyclerView.setLayoutManager(linearLayoutManager);

        newsAdapter = new NewsAdapter(newsItems, new NewsAdapter.OnNewsItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                ShowDetails(newsItems.get(position).getId(), position);
            }
        });
        newsRecyclerView.setAdapter(newsAdapter);
    }
}
