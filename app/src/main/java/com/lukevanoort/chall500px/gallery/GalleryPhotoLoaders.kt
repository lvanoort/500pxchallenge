package com.lukevanoort.chall500px.gallery

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.widget.ImageView
import coil.api.load
import com.lukevanoort.chall500px.photo.Photo
import javax.inject.Inject
import kotlin.random.Random

interface GalleryPhotoLoader {
    fun loadPhoto(view: ImageView, photo: Photo)
}

class MockGalleryPhotoLoader @Inject constructor(): GalleryPhotoLoader {
    override fun loadPhoto(view: ImageView, photo: Photo) {
        val rnd = Random(photo.id)
        view.setImageDrawable(ShapeDrawable(RectShape()).apply {
            paint.color = Color.argb(
                255,
                rnd.nextInt(256),
                rnd.nextInt(256),
                rnd.nextInt(256)
            )

            intrinsicHeight = photo.height
            intrinsicWidth = photo.width
        })
    }

}

class CoilPhotoLoader @Inject constructor(): GalleryPhotoLoader {
    override fun loadPhoto(view: ImageView, photo: Photo) {
        view.load(photo.thumbUrl) {
            crossfade(true)
            placeholder(ShapeDrawable(RectShape()).apply {
                paint.color = Color.argb(
                    0,
                    0,
                    0,
                    0
                )

                intrinsicHeight = photo.height
                intrinsicWidth = photo.width
            })
        }
    }
}

