package com.example.progressbardemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope



class MainActivity : AppCompatActivity() {

    private lateinit var downloadButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var imageView: ImageView
    private lateinit var restartButton: Button

    private val imageUrl = "https://animalgiftclub-static.myshopblocks.com/images/2019/03/contain/2048x2048/ad91f89f14a43481e85fe0809ebd5b5e.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        downloadButton = findViewById(R.id.downloadButton)
        progressBar = findViewById(R.id.progressBar)
        imageView = findViewById(R.id.imageView)
        restartButton = findViewById(R.id.restartButton)

        downloadButton.setOnClickListener {
            // Use lifecycleScope to launch the coroutine
            lifecycleScope.launch {
                downloadImage()
            }
        }

        restartButton.setOnClickListener {
            progressBar.visibility = View.GONE
            imageView.visibility = View.GONE
            restartButton.visibility = View.GONE
            downloadButton.visibility = View.VISIBLE
        }
    }



    private  suspend fun downloadImage() {
        progressBar.visibility = View.VISIBLE
        downloadButton.visibility = View.GONE

        val image = loadImageFromUrl(imageUrl)

        withContext(Dispatchers.Main) {
            if (image != null) {
                imageView.visibility = View.VISIBLE
                imageView.setImageBitmap(image)
                restartButton.visibility = View.VISIBLE
            } else {
                downloadButton.visibility = View.VISIBLE
            }

            progressBar.visibility = View.GONE
        }
    }



    private suspend fun loadImageFromUrl(url: String): Bitmap? = withContext(Dispatchers.IO) {
        return@withContext try {
            val inputStream = java.net.URL(url).openStream()
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

