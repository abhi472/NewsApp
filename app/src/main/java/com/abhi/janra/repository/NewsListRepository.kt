package com.abhi.janra.repository

import com.abhi.janra.common.Article

interface NewsListRepository {

    fun getAllArticles(json: String): List<Article>
}