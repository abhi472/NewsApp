package com.abhi.janra.usecase;

import android.os.Build
import androidx.annotation.RequiresApi
import com.abhi.janra.common.Article
import com.abhi.janra.repository.NewsListRepository
import com.abhi.janra.util
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsListUseCase @Inject constructor(private val repository: NewsListRepository) {

    suspend fun getArticles(json: String) = flow {
        emit(repository.getAllArticles(json))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun invokeUpdate(list: List<Article>): Flow<List<Article>> {
        val newList = list.toMutableList()
        val iterate = newList.listIterator()
        while (iterate.hasNext()) {
            val oldValue = iterate.next()
            iterate.set(oldValue.updateArticle(
                datePair = util.getAbbreviatedFromDateTime(dateTime = oldValue.publishedAt),
                title = oldValue.title, url = oldValue.url, urlToImage = oldValue.urlToImage,))
        }

       return flow { emit(newList) }
    }

    suspend fun invokeSort(list: List<Article>) = flow {
        emit(list.sortedBy { it.time })
    }
}