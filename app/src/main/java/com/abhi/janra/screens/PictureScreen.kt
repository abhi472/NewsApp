package com.abhi.janra.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.abhi.janra.R
import com.abhi.janra.common.Article
import com.abhi.janra.common.MainViewModel
import com.abhi.janra.ui.theme.JANRATheme

@Composable
fun PictureScreen(
    viewModel: MainViewModel,
    navController: NavController,
) {

    val state = viewModel.uiState.collectAsState().value
    var text by rememberSaveable { mutableStateOf("") }

    JANRATheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                            value = text,
                    onValueChange = {
                        text = it
                        viewModel.postEvent(it)
                    }, placeholder = { Text(text = "Search")}
                )
                ArticleList(articles = state.files)
            }
        }
    }
}

@Composable
fun NewsCard(article: Article) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = Color.White
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = stringResource(R.string.description),
                modifier = Modifier
                    .size(130.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit,
            )
            Column(Modifier.padding(8.dp)) {
                if (article.title.isNotEmpty()) {
                    Text(article.title,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp, modifier = Modifier.padding(10.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = article.publishedAt,
                    fontWeight = FontWeight.Light,
                    color = Color.Black,
                    fontSize = 12.sp, modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun ArticleList(articles: List<Article>) {

    LazyColumn {
        items(
            count = articles.size,
             itemContent = { index ->
                NewsCard(article = articles[index])
            }
        )
    }
}

