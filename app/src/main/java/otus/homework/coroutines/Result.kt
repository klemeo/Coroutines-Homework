package otus.homework.coroutines

import android.support.annotation.StringRes

sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Error(val msg: String? = null, @StringRes val resId: Int? = null, ) : Result<Nothing>()
}