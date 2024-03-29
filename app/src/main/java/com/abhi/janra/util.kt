
@file:JvmName("DateUtils")
package com.abhi.janra


import android.content.Context
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


object util {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAbbreviatedFromDateTime(dateTime: String,
                                   dateFormat: String = "yyyy-MM-ddThh:mm:ssZ",
                                   field: String = "dd MMM yyyy hh:mm aaa"): Pair<String, Long> {
        val output = SimpleDateFormat(field)
        try {
            val getAbbreviate = java.time.Instant.parse(dateTime)
            return Pair(output.format(Date.from(getAbbreviate)), getAbbreviate.epochSecond)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return Pair("", 0)
    }



    fun getJsonFromDir(context: Context, path: String): String = try {
            val file = context.assets.open(path)
            val bufferedReader = BufferedReader(InputStreamReader(file))
            val stringBuilder = StringBuilder()
            bufferedReader.useLines { lines ->
                lines.forEach {
                    stringBuilder.append(it)
                }
            }
            stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }


    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        Glide.with(view.context).load(url).into(view)
    }

}