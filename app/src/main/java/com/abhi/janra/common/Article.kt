package com.abhi.janra.common

data class Article(
    val publishedAt: String,
    val title: String,
    val url: String,
    val urlToImage: String? = "",
    val time: Long?
) {
    fun updateArticle(
        datePair: Pair<String, Long>,
        title: String,
        urlToImage: String?,
        url: String) = copy(
        publishedAt = datePair.first,
        title = title,
        url = url,
        urlToImage = urlToImage,
        time = datePair.second
    )


}