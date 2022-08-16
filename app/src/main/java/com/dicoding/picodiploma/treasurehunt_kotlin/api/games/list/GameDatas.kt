package com.dicoding.picodiploma.treasurehunt_kotlin.api.games.list

data class GameDatas(
    val id : String,
    val title : String,
    val sub_title : String,
    val description : String,
    val max_member : Int,
    val created_at : String,
    val updated_at : String,
    val banner : GameDatasBaner?,
    val _count : GameDatasCount?

)
