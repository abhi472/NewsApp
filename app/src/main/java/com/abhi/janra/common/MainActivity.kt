package com.abhi.janra.common

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.abhi.janra.ui.theme.JANRATheme
import com.abhi.janra.util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
       * Code changed and this activity now supports only coroutines
       * for rx implementation set MainActivityXML
       * */
        util.getJsonFromDir(this, "news.json").apply {
            viewModel.loadData(this)
        }


        setContent {
            JANRATheme {
                BottomNavigationBar(viewModel = viewModel)
            }
        }


    }
}


