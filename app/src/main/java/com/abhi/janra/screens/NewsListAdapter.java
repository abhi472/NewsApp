package com.abhi.janra.screens;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi.janra.R;
import com.abhi.janra.common.Article;
import com.abhi.janra.databinding.NewsListItemBinding;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {


    private List<Article> article;



    public NewsListAdapter(List<Article> article) {
        this.article = article;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        NewsListItemBinding employeeListItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.news_list_item, viewGroup, false);
        return new ViewHolder(employeeListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.newsListItemBinding.setArticle(article.get(i));


    }

    @Override
    public int getItemCount() {
        return article == null ? 0 : article.size();
    }

    public void setNews(List<Article> article) {
        this.article = article;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private NewsListItemBinding newsListItemBinding;

        public ViewHolder(@NonNull NewsListItemBinding newsListItemBinding) {
            super(newsListItemBinding.getRoot());

            this.newsListItemBinding = newsListItemBinding;
        }
    }


}


