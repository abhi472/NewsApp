package com.abhi.janra

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abhi.janra.common.Article
import com.abhi.janra.common.MainViewModel
import com.abhi.janra.repository.NewsListRepository
import com.abhi.janra.states.MediaState
import com.abhi.janra.usecase.NewsListUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    private lateinit var updateArticles: List<Article>

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private lateinit var mockUseCase: NewsListUseCase

    @Mock
    private lateinit var mockRepository: NewsListRepository

    private lateinit var article1:Article

    private lateinit var article2:Article

    private lateinit var json: String

    private lateinit var viewModel: MainViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        mockUseCase = NewsListUseCase(mockRepository)
        viewModel = MainViewModel(mockUseCase)
        json = """
              {"articles": [
    {
      "publishedAt": "2024-01-10T22:41:25Z",
      "title": "SEC approves bitcoin ETFs (for real this time)",
      "url": "https://www.engadget.com/sec-approves-bitcoin-etfs-for-real-this-time-224125584.html",
      "urlToImage": "https://s.yimg.com/ny/api/res/1.2/n6iLNJ_9dtK.fT6WAXK1sA--/YXBwaWQ9aGlnaGxhbmRlcjt3PTEyMDA7aD03OTU-/https://s.yimg.com/os/creatr-uploaded-images/2024-01/3edf5140-afdd-11ee-bf7c-7918e1b9d963"
    },
    {
      "publishedAt": "2024-01-11T18:18:13Z",
      "title": "Why Crypto Idealogues Won’t Touch Bitcoin ETFs",
      "url": "https://www.wired.com/story/bitcoin-etf-cryptocurrencies-split/",
      "urlToImage": "https://media.wired.com/photos/65a0305c4aaf02fdf493f220/191:100/w_1280,c_limit/Not-Everyone-Is-Jazzed-About-Bitcoin-ETFs-Business-1299911534.jpg"
    }
    ]
    }
        """.trimIndent()
        article1 = Article(publishedAt="2024-01-10T23:41:25Z",
        title =  "SEC approves bitcoin ETFs (for real this time)",
        url =  "https://www.engadget.com/sec-approves-bitcoin-etfs-for-real-this-time-224125584.html",
        urlToImage =  "https://s.yimg.com/ny/api/res/1.2/n6iLNJ_9dtK.fT6WAXK1sA--/YXBwaWQ9aGlnaGxhbmRlcjt3PTEyMDA7aD03OTU-/https://s.yimg.com/os/creatr-uploaded-images/2024-01/3edf5140-afdd-11ee-bf7c-7918e1b9d963",
            null
        )

        article2 = Article(publishedAt="2024-01-10T22:41:25Z",
            title =  "SEC approves bitcoin ETFs (for real this time)",
            url =  "https://www.engadget.com/sec-approves-bitcoin-etfs-for-real-this-time-224125584.html",
            urlToImage =  "https://s.yimg.com/ny/api/res/1.2/n6iLNJ_9dtK.fT6WAXK1sA--/YXBwaWQ9aGlnaGxhbmRlcjt3PTEyMDA7aD03OTU-/https://s.yimg.com/os/creatr-uploaded-images/2024-01/3edf5140-afdd-11ee-bf7c-7918e1b9d963",
            null
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testLoadData() = runTest {
        val intSharedFlow = MutableStateFlow(MediaState())

        val articles: List<Article> = listOf(article1, article2)

        val flow = flowOf(articles)

        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        backgroundScope.launch(dispatcher) { mockUseCase.getArticles(json)
            .collect{intSharedFlow.value = MediaState(process = 1)}}
        advanceUntilIdle()

        assertEquals(1,intSharedFlow.value.process)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testUpdateAndSortData() = runTest {
        val intSharedFlow = MutableStateFlow(MediaState())

        val articles: List<Article> = listOf(article1, article2)


        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        backgroundScope.launch(dispatcher) { mockUseCase.invokeUpdate(articles)
            .collect{
                updateArticles = it
                intSharedFlow.value = MediaState(process = 2)}}
        advanceUntilIdle()

        assertEquals(2,intSharedFlow.value.process)
        assertNotNull(updateArticles[0].time)

        backgroundScope.launch(dispatcher) { mockUseCase.invokeSort(updateArticles)
            .collect{ intSharedFlow.value = MediaState(process = 3)}}
        advanceUntilIdle()

        assertEquals(3,intSharedFlow.value.process)
        assert(updateArticles[0].time!! > updateArticles[1].time!!)
    }



}