package com.dicoding.picodiploma.treasurehunt_kotlin.api.games.detail


data class GameData(
    val id : String,
    val title : String,
    val sub_title : String,
    val description : String,
    val max_member : Int,
    val created_at : String,
    val updated_at : String,
    val banner : GameBanner?,
    val galleries : List<GalleriesItem>?,
    val _count : Count?
)
