package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val di by lazy { DiContainer() }
    private lateinit var catsPresenter: CatsPresenter
    private lateinit var model: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /**
         * Инициализируем либо презентер либо viewModel
         */
        initModel()
        initPresenter()
    }

    private fun initModel() {
        model = CatsViewModel(
            di.serviceFact,
            di.serviceImage
        )
        model.state.observe(this) {
            onStateChanged(it)
        }
        model.also { binding.catsInfoView.model = it }
    }

    private fun initPresenter() {
        catsPresenter = CatsPresenter(
            binding.catsInfoView,
            di.serviceFact,
            di.serviceImage
        )
        binding.catsInfoView.presenter = catsPresenter
    }

    private fun onStateChanged(result: Result<CatPresModel>) {
        when (result) {
            is Result.Success -> binding.catsInfoView.populate(result.value)
            is Result.Error -> onError(result)
        }
    }

    private fun onError(result: Result.Error) {
        when {
            result.msg != null -> binding.catsInfoView.showToast(result.msg)
            result.resId != null -> binding.catsInfoView.showToast(result.resId)
        }
    }


}