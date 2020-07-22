package com.lukevanoort.chall500px.photo

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.widget.ImageView
import coil.api.load
import javax.inject.Inject
import kotlin.random.Random

interface PhotoLoader {
    fun loadPhoto(view: ImageView, photo: Photo, useLarge: Boolean)
}

class MockPhotoLoader @Inject constructor(): PhotoLoader {
    override fun loadPhoto(view: ImageView, photo: Photo, useLarge: Boolean) {
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

class CoilPhotoLoader @Inject constructor(): PhotoLoader {
    override fun loadPhoto(view: ImageView, photo: Photo, useLarge: Boolean) {
        view.load(if(useLarge){photo.fullsizeUrl} else {photo.thumbUrl}) {
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

