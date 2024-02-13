package com.abhi.janra.usecase;


import android.os.Build;

import com.abhi.janra.common.Article;
import com.abhi.janra.repository.NewsListRepository;
import com.abhi.janra.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewsListRxUseCase {

    private final NewsListRepository repository;
   @Inject
    public NewsListRxUseCase(NewsListRepository repository) {
        this.repository = repository;
    }

    public Observable<List<Article>> getUpdatedList(String json){
      return getArticlesUpdated(json).collect(Collectors.toList()).map(articles -> {

          List<Article> sortedList = new ArrayList<>(articles);
          sortedList.sort(Comparator.comparing(Article::getTime));
          return sortedList;
      }).toObservable()
              .subscribeOn(Schedulers.computation())
              .observeOn(AndroidSchedulers.mainThread());
    }

    public @NonNull Observable<Article> getArticles(String json) {
       return Observable.fromIterable(repository.getAllArticles(json));

    }

    public @NonNull Observable<Article> getArticlesUpdated(String json) {
       return getArticles(json).map(article -> {
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               return article.updateArticle(util.INSTANCE.getAbbreviatedFromDateTime(article.getPublishedAt(),
                       "yyyy-MM-ddThh:mm:ssZ",
                       "dd MMM yyyy hh:mm aaa"),
                       article.getTitle(),
                       article.getUrlToImage() == null ? "": article.getUrlToImage(), article.getUrl());
           } else return article;
       });
    }
}
