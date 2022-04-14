package otus.homework.coroutines

import android.content.Context
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.databinding.CatsViewBinding

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    private val binding: CatsViewBinding by lazy {
        CatsViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    var model: CatsViewModel? = null
    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding.button.setOnClickListener {
            model?.updateData()

            presenter?.onInitComplete()
        }
    }

    override fun populate(model: CatPresModel) {
        binding.factTextView.text = model.fact

        Picasso.get()
            .load(model.pictureUrl)
            .into(binding.picture)
    }

    override fun showToast(messageRes: Int) {
        Toaster(context).showToast(messageRes)
    }

    override fun showToast(messageText: String) {
        Toaster(context).showToast(messageText)
    }
}

interface ICatsView {
    fun populate(model: CatPresModel)
    fun showToast(@StringRes messageRes: Int)
    fun showToast(messageText: String)
}