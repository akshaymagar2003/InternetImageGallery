package com.example.api

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.example.api.Network.Photo
import com.squareup.picasso.Picasso

class PhotosAdapter(private val context: Context, private val photos: List<Photo>) : BaseAdapter() {

    override fun getCount(): Int {
        return photos.size
    }

    override fun getItem(position: Int): Any {
        return photos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val photo = photos[position]
        val view: View

        if (convertView == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.each_image, null)
        } else {
            view = convertView
        }

        val imageView = view.findViewById<ImageView>(R.id.imageView)

        // Load image using Glide
        Picasso.get().load(photo.url_s).into(imageView)


        return view
    }

}


