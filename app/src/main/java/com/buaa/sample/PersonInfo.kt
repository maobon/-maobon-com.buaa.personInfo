package com.buaa.sample

data class PersonInfo(
    val username: String,
    val phone: String,
    val gender: Int,
    val fav_lesson: String,
    val avatar_base64: String? = null
)