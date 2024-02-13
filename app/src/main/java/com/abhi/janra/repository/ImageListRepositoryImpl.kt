package com.abhi.janra.repository

import com.abhi.janra.common.Article
import com.abhi.janra.common.NewsItemData
import com.google.gson.Gson
import javax.inject.Inject

class NewsListRepositoryImpl @Inject constructor(): NewsListRepository {
    override fun getAllArticles(json: String): List<Article> {
        val gson = Gson()
        val item = gson.fromJson(json, NewsItemData::class.java)

        return item?.articles ?: emptyList()
    }



}