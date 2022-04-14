package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsView: CatsView,
    private val factsService: CatsService,
    private val picsService: CatsService,
) {

    private val scope = PresenterScope()

    private val scopeExceptionHandler = CoroutineExceptionHandler { _, ex ->
        when (ex) {
            is SocketTimeoutException -> {
                catsView.showToast("Не удалось получить ответ от сервера")
            }
            else -> {
                catsView.showToast("${ex.message}")
                CrashMonitor.trackWarning()
            }
        }
    }

    fun onInitComplete() {
        scope.launch(scopeExceptionHandler) {
            try {
                val fact = async { factsService.getCatFact() }
                val image = async { picsService.getCatImage() }
                val state = CatPresModel(
                    fact.await().text,
                    image.await().file
                )
                catsView.populate(state)
            } catch (ex: SocketTimeoutException) {
                catsView.showToast("${ex.message}")
                CrashMonitor.trackWarning()
            }
        }
    }

}