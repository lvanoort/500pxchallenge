package com.lukevanoort.chall500px.gallery

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AnyThread
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lukevanoort.chall500px.R
import com.lukevanoort.chall500px.databinding.GalleryItemViewBinding
import com.lukevanoort.chall500px.getActivityComponent
import com.lukevanoort.chall500px.photo.Photo
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class GalleryDisplayView : SwipeRefreshLayout {
    private var vm: GalleryViewModel? = null
    private var stateSub: Disposable? = null

    @Inject
    lateinit var photoLoader : GalleryPhotoLoader

    private lateinit var recyclerView: RecyclerView

    private var nextState: GalleryViewState = GalleryViewState.IdleGallery(listOf())
    private var currentState: GalleryViewState? = null

    constructor(context: Context) : super(context) {init(context)}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {init(context)}

    private fun init(context: Context) {
        getActivityComponent()?.galleryViewComponent()?.inject(this)

        recyclerView = RecyclerView(context)
        recyclerView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addView(recyclerView)

        setOnRefreshListener {
            vm?.reload()
        }

        val sglm = StaggeredGridLayoutManager(
            // TODO: make this more intelligent so span count is a function of screen size
            3,
            StaggeredGridLayoutManager.VERTICAL
        )
        sglm.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        recyclerView.layoutManager = sglm
        recyclerView.adapter = GalleryDisplayViewAdapter()
        // itemAnimator is disabled to avoid a weird UI behaviour where the recyclerview
        // shuffles the items back into their original locations on a quick scroll
        // TODO: find a better way to deal with this than preventing all animations
        recyclerView.itemAnimator = null

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // check to see if we're going down and can go no further
                if (dy > 0 && !recyclerView.canScrollVertically(1)) {
                    vm?.loadMore()
                }
            }
        })
    }

    @UiThread
    private fun reset() {
        val localNext = nextState
        val previousCurrent = currentState

        currentState = localNext
        isRefreshing = when(localNext) {
            is GalleryViewState.IdleGallery -> false
            is GalleryViewState.LoadingGallery -> true
        }
        if (currentState?.pictures != previousCurrent?.pictures) {
            recyclerView.adapter?.notifyDataSetChanged()
            if (!recyclerView.canScrollVertically(1)) {
                vm?.loadMore()
            }
        }
    }

    @AnyThread
    private fun setState(viewState: GalleryViewState) {
        nextState = viewState
        post {
            reset()
        }
    }

    @UiThread
    fun attachViewModel(vm: GalleryViewModel) {
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

    inner class GalleryVH(view: View) : RecyclerView.ViewHolder(view){
        private var binding : GalleryItemViewBinding = GalleryItemViewBinding.bind(view)
        private var currentItem : Photo? = null

        init {
            binding.ivGalleryItem.setOnClickListener {
                val localCurrent = currentItem
                if (localCurrent != null) {
                    vm?.entrySelected(localCurrent)
                }
            }
        }

        fun displayItem(item: Photo) {
            currentItem = item
            photoLoader.loadPhoto(binding.ivGalleryItem,item)
        }

    }

    inner class GalleryDisplayViewAdapter : RecyclerView.Adapter<GalleryVH>(){
        init {
            setHasStableIds(true)
        }

        override fun getItemId(position: Int): Long {
            return when(val localCurrent = currentState) {
                is GalleryViewState.IdleGallery -> {
                    localCurrent.pictures[position].id.toLong()
                }
                is GalleryViewState.LoadingGallery -> {
                    localCurrent.pictures[position].id.toLong()
                }
                else -> RecyclerView.NO_ID
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryVH {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.gallery_item_view,parent,false)
            return GalleryVH(v)
        }

        override fun getItemCount(): Int {
            return when(val localCurrent = currentState) {
                is GalleryViewState.IdleGallery -> {
                    localCurrent.pictures.size
                }
                is GalleryViewState.LoadingGallery  -> {
                    localCurrent.pictures.size
                }
                null -> 0
            }
        }

        override fun onBindViewHolder(holder: GalleryVH, position: Int) {
            when(val localCurrent = currentState) {
                is GalleryViewState.IdleGallery -> {
                    holder.displayItem(localCurrent.pictures[position])
                }
                is GalleryViewState.LoadingGallery -> {
                    holder.displayItem(localCurrent.pictures[position])
                }
            }

        }

    }
}