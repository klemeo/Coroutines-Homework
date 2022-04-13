package otus.homework.coroutines

class CatsPresenter(
    private val catsView: CatsView,
    private val model: CatsViewModel) {

    fun onBtnClick() {
        model.updateData()
    }

    fun onStateChanged(result: Result<CatPresModel>) {
        when (result) {
            is Result.Success -> catsView.populate(result.value)
            is Result.Error -> onError(result)
        }
    }

    private fun onError(result: Result.Error) {
        when {
            result.msg != null -> catsView.showToast(result.msg)
            result.resId != null -> catsView.showToast(result.resId)
        }
    }

}