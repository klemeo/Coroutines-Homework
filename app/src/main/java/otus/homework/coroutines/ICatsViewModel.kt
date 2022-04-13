package otus.homework.coroutines

import androidx.lifecycle.LiveData

interface ICatsViewModel{
    val state: LiveData<Result<CatPresModel>>
    fun updateData()
}