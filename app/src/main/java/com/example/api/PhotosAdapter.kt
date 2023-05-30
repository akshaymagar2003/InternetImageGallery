package com.example.api

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
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
        val photo = getItem(position) as Photo

        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.each_image, parent, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        Glide.with(context).load(photo.url_s).into(imageView);

        return view
    }
}