package com.example.api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.api.Network.ApiService
import com.example.api.Network.Photo
import com.example.api.Network.apiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var gridView: GridView

    private val photosList: ArrayList<Photo> = ArrayList()
    private lateinit var photosAdapter: PhotosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gridView = findViewById(R.id.gridView)

        // Create adapter and set it to the GridView
        photosAdapter = PhotosAdapter(this, photosList)
        gridView.adapter = photosAdapter

        // Call the API and update the adapter with the retrieved photos
        fetchRecentPhotos()
    }

    private fun fetchRecentPhotos() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val flickrService = Retrofit.Builder()
                    .baseUrl("https://api.flickr.com/services/rest/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)

                val photosResponse = withContext(Dispatchers.IO) {
                    flickrService.getRecentPhotos(
                        method = "flickr.photos.getRecent",
                        perPage = 20,
                        page = 1,
                        apiKey = "6f102c62f41998d151e5a1b48713cf13",
                        format = "json",
                        noJsonCallback = 1,
                        extras = "url_s"
                    )
                }

                if (photosResponse.body()?.stat == "ok") {
                    val photos = photosResponse.body()!!.photos.photo
                    photosList.addAll(photos)
                    photosAdapter.notifyDataSetChanged()
                } else {
                    // Handle API error
                }
            } catch (e: Exception) {
                // Handle network failure
            }
        }
    }
}