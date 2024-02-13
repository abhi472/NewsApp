package com.abhi.janra.states

import com.abhi.janra.common.Article

data class MediaState (

    val files: List<Article> = emptyList(),
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val process: Int = 0,
)