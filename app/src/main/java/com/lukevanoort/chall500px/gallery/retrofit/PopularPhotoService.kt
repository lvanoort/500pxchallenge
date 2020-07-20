package com.lukevanoort.chall500px.gallery.retrofit

import com.lukevanoort.chall500px.photo.Photo
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularPhotoService {
    @GET("photos?feature=popular")
    fun getPopularPhotos(
        @Query("consumer_key") consumerKey: String,
        @Query("page") page: Int
    ) : Observable<PhotoListResponse>
}

data class PhotoResponse (
    override val id: Int,
    override val width: Int,
    override val height: Int,
    val image_url: List<String>
) : Photo {
    override val thumbUrl: String
        get() = image_url.firstOrNull() ?: ""
}

data class PhotoListResponse (
    val current_page: Int,
    val total_pages: Int,
    val total_items: Int,
    val photos: List<PhotoResponse>
)