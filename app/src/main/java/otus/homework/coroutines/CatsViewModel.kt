package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val factsService: CatsService,
    private val picsService: CatsService,
) : ViewModel(), ICatsViewModel {

    private val _state = MutableLiveData<Result<CatPresModel>>()
    override val state = _state as LiveData<Result<CatPresModel>>

    private var loadFactsJob: Job? = null

    private val scopeExceptionHandler = CoroutineExceptionHandler { _, ex ->
        when (ex) {
            is SocketTimeoutException -> {
                _state.postValue(Result.Error(msg = "Не удалось получить ответ от сервера"))
            }
            else -> {
                _state.postValue(Result.Error(msg = "${ex.message}"))
                CrashMonitor.trackWarning()
            }
        }
    }

    init {
        updateData()
    }

    fun updateData() {
        loadFactsJob?.cancel()
        loadFactsJob = viewModelScope.launch(scopeExceptionHandler) {
            val fact = async { factsService.getCatFact() }
            val image = async { picsService.getCatImage() }
            _state.value = Result.Success(
                CatPresModel(
                    fact.await().text,
                    image.await().file
                )
            )
        }
    }

}