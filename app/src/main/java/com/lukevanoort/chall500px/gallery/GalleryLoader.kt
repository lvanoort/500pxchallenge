package com.lukevanoort.chall500px.gallery

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.widget.ImageView
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

            intrinsicHeight = when(rnd.nextInt(3)) {
                0 -> 1024
                1 -> 500
                2 -> 650
                else -> 800
            }
            intrinsicWidth =when(rnd.nextInt(3)) {
                0 -> 1024
                1 -> 500
                2 -> 650
                else -> 800
            }
        })
    }

}