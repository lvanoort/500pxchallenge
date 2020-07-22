package com.lukevanoort.chall500px.detail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.AnyThread
import androidx.annotation.UiThread
import androidx.constraintlayout.widget.ConstraintLayout
import com.lukevanoort.chall500px.databinding.DetailItemViewBinding
import com.lukevanoort.chall500px.getActivityComponent
import com.lukevanoort.chall500px.photo.PhotoLoader
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class DetailView : ConstraintLayout {
    private var vm: DetailViewModel? = null
    private var stateSub: Disposable? = null

    private var nextState: DetailViewState = DetailViewState.Loading
    private var currentState: DetailViewState? = null

    private var displayingControls = false
    private lateinit var binding: DetailItemViewBinding

    @Inject
    lateinit var photoLoader: PhotoLoader

    constructor(context: Context) : super(context) { init(context) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(context) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(context) }

    private fun init(context: Context) {
        getActivityComponent()?.detailViewComponent()?.inject(this)
        binding = DetailItemViewBinding.inflate(LayoutInflater.from(context),this)
        updateControlVisibility()

        binding.ivBack.setOnClickListener {
            vm?.exit()
        }

        binding.ivDetailView.setOnClickListener {
            displayingControls = !displayingControls
            updateControlVisibility()
        }
    }

    @UiThread
    private fun updateControlVisibility() {
        if (displayingControls) {
            binding.grpControls.visibility = View.VISIBLE
        } else {
            binding.grpControls.visibility = View.GONE
        }
    }

    @UiThread
    private fun reset() {
        val localNext = nextState
        val previousCurrent = currentState

        if (localNext != previousCurrent) {
            displayingControls = false
            updateControlVisibility()
            when(localNext) {
                is DetailViewState.LoadedData -> {
                    photoLoader.loadPhoto(
                        binding.ivDetailView,
                        localNext.photo,
                        true
                    )
                    binding.tvTitle.text = localNext.photo.name
                    binding.tvAuthor.text = localNext.photo.photographerName
                    binding.tvDescription.text = localNext.photo.description
                }
                DetailViewState.Loading -> {
                    binding.ivDetailView.setImageDrawable(null)
                }
            }
            currentState = localNext
        }
    }

    @AnyThread
    private fun setState(viewState: DetailViewState) {
        nextState = viewState
        post {
            reset()
        }
    }

    @UiThread
    fun attachViewModel(vm: DetailViewModel) {
        stateSub?.dispose()
        stateSub = vm.getState().subscribe {
            setState(it)
        }
        this.vm = vm
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stateSub?.dispose()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        stateSub?.dispose() // no harm in calling this again
        val localVm = vm
        if (localVm != null) {
            stateSub = localVm.getState().subscribe {
                setState(it)
            }
        }
    }
}