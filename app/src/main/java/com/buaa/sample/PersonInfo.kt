package com.buaa.sample

/**
 * create by xin on 2022-3-28
 */

data class PersonInfo(
    val username: String,
    val phone: String,
    val gender: Int,
    val fav_lesson: String,
    val avatar_base64: String? = null
)