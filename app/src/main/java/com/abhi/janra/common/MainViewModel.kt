package com.abhi.janra.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhi.janra.SingleLiveEvent
import com.abhi.janra.states.MediaState
import com.abhi.janra.usecase.NewsListRxUseCase
import com.abhi.janra.usecase.NewsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsListUseCase: NewsListUseCase,
    private val newsListRxUseCase: NewsListRxUseCase
): ViewModel() {

    private lateinit var list: List<Article>
    private var disposable: CompositeDisposable = CompositeDisposable()
    private val _uiState = MutableStateFlow(MediaState())
    val uiState: StateFlow<MediaState> = _uiState.asStateFlow()
    val liveEvent: SingleLiveEvent<List<Article>> = SingleLiveEvent()


    private val _search = MutableStateFlow<String>("")

    init {
        observe()

    }

     fun postEvent(event: String) {
        _search.value = event
    }

    @OptIn(FlowPreview::class)
    private fun observe() {
        _search.debounce(500)
            .filter { this::list.isInitialized }
            .onEach {
                updateUiState(files = list.filter { it1-> it1.title.contains(it, true) })
                liveEvent.value = list.filter { it1-> it1.title.contains(it, true) }
            }
            // flowOn(), catch{}
            .launchIn(viewModelScope)

    }

    fun loadDataRx(json:String) {
        disposable.add(newsListRxUseCase.getUpdatedList(json).subscribe({
            liveEvent.value = it
            list = it
        }, {

        }))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData(json: String) {
        viewModelScope.launch {
            newsListUseCase.getArticles(json)
                .flowOn(Dispatchers.IO)
                .catch {
                     updateUiState(isError = true, isLoading = false, process = -1 )

                }
                .collect {
                    updateUiState(isError = true, isLoading = false, process = 1 )
                    newsListUseCase.invokeUpdate(it)
                      .catch {
                          updateUiState(isError = true, isLoading = false, process = -2)


                  }.collect {
                            updateUiState(isError = true, isLoading = false, process = 2)
                            newsListUseCase.invokeSort(it)
                          .catch {
                              updateUiState(isError = true, isLoading = false, process = -3)
                          }.collect {
                              updateUiState(files = it, isError = false, isLoading = false, process = 3)
                              list = it
                          }
                  }
                }
        }
    }

    private fun updateUiState(
        files: List<Article> = uiState.value.files,
        isError: Boolean = uiState.value.isError,
        isLoading: Boolean = uiState.value.isLoading,
        process: Int = uiState.value.process,
    ) {
        _uiState.value = uiState.value.copy(
            files = files,
            isError = isError,
            isLoading = isLoading,
            process = process,
        )
    }

    fun getMockList(): List<String> {
        val list: ArrayList<String> = ArrayList()
        for (i in 0..9) {
            list.add("Lorem Ipsum")
        }
        return list
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}