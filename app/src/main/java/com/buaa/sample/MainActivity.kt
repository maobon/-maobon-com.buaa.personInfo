package com.buaa.sample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.buaa.sample.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private var picUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        initViews()
    }

    private fun initViews() {
        activityMainBinding.etPhone.addTextChangedListener {
            printLog(it.toString())

            val len = it.toString().length
            activityMainBinding.tvNumCount.text = String.format("%d/11", len)
        }

        activityMainBinding.ivAvatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                REQUEST_GET_SINGLE_FILE
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_URI, picUri.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setAvatar(Uri.parse(savedInstanceState.getString(KEY_URI, null)))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == REQUEST_GET_SINGLE_FILE) {
            setAvatar(data!!.data!!)
        }
    }

    private fun setAvatar(uri: Uri) {
        this.picUri = uri;
        val transform = RequestOptions().transform(CenterCrop(), RoundedCorners(360))
        val imageView = activityMainBinding.ivAvatar
        Glide.with(imageView.context).load(uri).apply(transform).into(imageView)
    }
}