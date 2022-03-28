package com.buaa.sample

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.buaa.sample.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private var picUri: Uri? = null

    private var avatarBase64: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        initViews()
    }

    private fun initViews() {
        activityMainBinding.btnSubmit.setOnClickListener {
            try {
                checkInputsValidate()
            } catch (e: IllegalArgumentException) {
                snack(activityMainBinding.root, e.message!!)
            }

            printLog("ok 可以提交了")

            val gender = if (activityMainBinding.rbMela.isChecked) 1 else 0
            val personInfo = createPersonInfo(
                activityMainBinding.etUsername.text.toString(),
                activityMainBinding.etPhone.text.toString(),
                gender,
                buildCheckBoxesResult(),
                avatarBase64
            )
            printLog(personInfo.toString())

            // activityMainBinding.ivAvatar
        }

        activityMainBinding.etPhone.addTextChangedListener {
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
        Glide.with(imageView.context)
            .asBitmap()
            .load(uri)
            .apply(transform)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imageView.setImageBitmap(resource)

                    val createScaledBitmap = Bitmap.createScaledBitmap(resource, 80, 80, false)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    createScaledBitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        50,
                        byteArrayOutputStream
                    )

                    val byteArray = byteArrayOutputStream.toByteArray();
                    avatarBase64 = Base64.encodeToString(byteArray, Base64.URL_SAFE)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun checkInputsValidate() {
        val username = activityMainBinding.etUsername.text.toString()
        if (TextUtils.isEmpty(username))
            throw IllegalArgumentException("username is null")

        val phone = activityMainBinding.etPhone.text.toString()
        if (TextUtils.isEmpty(phone))
            throw IllegalArgumentException("phone number is null")

        if (isPhoneInvalidate(phone))
            throw IllegalArgumentException("phone number is invalidate")

        val cbJavaChecked = activityMainBinding.cbJava.isChecked
        val cbAndroidChecked = activityMainBinding.cbAndroid.isChecked
        val cbEnglishChecked = activityMainBinding.cbEnglish.isChecked
        val cbMathChecked = activityMainBinding.cbMath.isChecked
        if (!cbJavaChecked && !cbAndroidChecked && !cbEnglishChecked && !cbMathChecked)
            throw IllegalArgumentException("check boxes need checked once at least")
    }

    private fun buildCheckBoxesResult(): String {
        val stringBuilder = StringBuilder()
        if (activityMainBinding.cbJava.isChecked)
            stringBuilder.append("Java")
        if (activityMainBinding.cbAndroid.isChecked)
            stringBuilder.append("|Android")
        if (activityMainBinding.cbEnglish.isChecked)
            stringBuilder.append("|English")
        if (activityMainBinding.cbMath.isChecked)
            stringBuilder.append("|Math")
        var sb = stringBuilder.toString()
        if (sb.indexOf("|") == 0)
            sb = sb.substring(1, sb.length)
        return sb
    }

    private fun createPersonInfo(
        username: String,
        phone: String,
        gender: Int,
        favLesson: String,
        avatar: String? = null
    ) = PersonInfo(username, phone, gender, favLesson, avatar)
}