package com.example.api

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.postDelayed
import com.example.api.Network.ApiService
import com.example.api.Network.Photo
import com.example.api.Network.apiService
import com.example.api.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.time.Duration.Companion.seconds

class MainActivity : AppCompatActivity() {
    private lateinit var gridView: GridView
    lateinit var binding : ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    private val photosList: ArrayList<Photo> = ArrayList()
    private lateinit var photosAdapter: PhotosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val progressDialog= ProgressDialog(this)
        progressDialog.setMessage("Loading....")
        progressDialog.show()
        Handler(Looper.getMainLooper()).postDelayed(3000){
            progressDialog.dismiss()
        }
        binding.apply {
            toggle = ActionBarDrawerToggle(this@MainActivity, binding.drawerLayout, R.string.open, R.string.close)
            binding.drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.firstItem -> {
//                      val intent =Intent(this@MainActivity,MainActivity::class.java)
//                        startActivity(intent)
                        Toast.makeText(this@MainActivity,"Homepage Clicked",Toast.LENGTH_SHORT).show()
                        drawerLayout.close()
                    }

                }
                true
            }

        }


        gridView = findViewById(R.id.gridView)

        photosAdapter = PhotosAdapter(this@MainActivity, photosList)
        gridView.adapter = photosAdapter


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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }
}