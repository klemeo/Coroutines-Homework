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
        initModel()
        initPresenter()
    }

    private fun initModel() {
        model = CatsViewModel(
            di.serviceFact,
            di.serviceImage
        )
        model.state.observe(this) { catsPresenter.onStateChanged(it) }
    }

    private fun initPresenter() {
        catsPresenter = CatsPresenter(binding.catsInfoView, model)
        binding.catsInfoView.presenter = catsPresenter
    }

}