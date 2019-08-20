package com.developercat.newsapp;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NewsItem> mainItems;
    private OnNewsItemClickListener listener;
    private int selectedPosition = -1;

    public NewsAdapter(List<NewsItem> mainItems, OnNewsItemClickListener listener) {
        this.mainItems = mainItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_row, viewGroup, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        NewsViewHolder viewHolder = (NewsViewHolder) holder;
        NewsItem item = mainItems.get(position);
        Picasso.get().load(item.getImage()).into(viewHolder.image);
        viewHolder.title.setText(item.getTitle());
        viewHolder.text.setText(item.getText());
        viewHolder.time.setText(item.getTime());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(position);
            }
        });

        if (position == selectedPosition) {
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        else {
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    public void SetSelected(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mainItems.size();
    }

    public interface OnNewsItemClickListener {
        public void onItemClicked(int position);
    }
}
