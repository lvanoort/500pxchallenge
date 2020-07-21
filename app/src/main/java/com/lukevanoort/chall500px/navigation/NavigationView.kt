package com.lukevanoort.chall500px.navigation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AnyThread
import androidx.annotation.UiThread
import com.lukevanoort.chall500px.detail.DetailView
import com.lukevanoort.chall500px.gallery.GalleryDisplayView
import com.lukevanoort.chall500px.gallery.GalleryViewModel
import com.lukevanoort.chall500px.gallery.GalleryViewState
import com.lukevanoort.chall500px.getActivityComponent
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject
import javax.inject.Provider

class NavigationView : FrameLayout, BackHandler {
    private var vm: NavigationViewModel? = null
    private var stateSub: Disposable? = null

    private var nextState: NavigationViewState? = null
    private var currentState: NavigationViewState? = null

    @Inject
    lateinit var galleryViewModelProvider: Provider<GalleryViewModel>
    private val galleryView: GalleryDisplayView by lazy {
        GalleryDisplayView(context).also {
            it.attachViewModel(galleryViewModelProvider.get())
        }
    }

    private val detailView: DetailView by lazy {
        DetailView(context)
    }

    private var currentView: View? = null

    constructor(context: Context) : super(context) { init(context) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(context) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(context) }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) { init(context) }

    private fun init (context: Context) {
        getActivityComponent()?.navigationViewComponent()?.inject(this)
    }

    @UiThread
    private fun reset() {
        val localNext = nextState
        val previousCurrent = currentState

        if (localNext != previousCurrent) {
            removeAllViews()
            when(localNext) {
                NavigationViewState.Gallery -> {
                    currentView = galleryView
                    addView(galleryView)
                }
                is NavigationViewState.Detail -> {
                    currentView = detailView
                    addView(detailView)
                }
            }
            currentState = localNext
        }
    }

    @AnyThread
    private fun setState(viewState: NavigationViewState) {
        nextState = viewState
        post {
            reset()
        }
    }

    @UiThread
    fun attachViewModel(vm: NavigationViewModel) {
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

    override fun handleBack(): Boolean {
        val localCurrent = currentView
        return if (localCurrent is BackHandler) {
            if(localCurrent.handleBack()) {
                true
            } else {
                vm?.goBack() ?: false
            }
        } else {
            vm?.goBack() ?: false
        }
    }
}