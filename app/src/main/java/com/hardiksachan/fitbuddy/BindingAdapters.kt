package com.hardiksachan.fitbuddy

import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

fun bindImage(imgView: ImageView, imgUrl : String?){
    imgUrl?.let{
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .override(100, 100)


        Glide.with(imgView.context)
            .load(imgUri)
            .apply(requestOptions)
            .thumbnail(0.2f)
            .into(imgView)
    }
}