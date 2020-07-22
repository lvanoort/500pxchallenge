package com.lukevanoort.chall500px.photo

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.ConcurrentHashMap

interface IndividualPhotoService {
    @GET("photos/{id}")
    fun getPhoto(
        @Path("id") photoId: Int,
        @Query("consumer_key") consumerKey: String
    ) : Observable<PhotoDetailResponse>
}

interface PopularPhotoService {
    @GET("photos?feature=popular")
    fun getPopularPhotos(
        @Query("consumer_key") consumerKey: String,
        @Query("page") page: Int
    ) : Observable<PhotoListResponse>
}

data class UserDataResponse(
    val id: Int,
    val fullname: String
)

data class PhotoResponse (
    override val id: Int,
    override val width: Int,
    override val height: Int,
    override val name: String,
    override val description: String,
    val image_url: List<String>,
    val user: UserDataResponse
) : Photo {
    override val thumbUrl: String
        get() = image_url.firstOrNull() ?: ""
    override val fullsizeUrl: String
        get() = thumbUrl
    override val photographerName: String
        get() = user.fullname
}

data class PhotoListResponse (
    val current_page: Int,
    val total_pages: Int,
    val total_items: Int,
    val photos: List<PhotoResponse>
)

data class PhotoDetailResponse (
    val photo: PhotoResponse
)