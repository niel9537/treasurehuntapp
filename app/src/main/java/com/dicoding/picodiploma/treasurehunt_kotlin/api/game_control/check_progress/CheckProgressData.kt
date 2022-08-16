package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.check_progress

import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.next_flow.Flow
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.next_flow.Post

data class CheckProgressData(
    val latest_post : Post?,
    val next_post : Post?,
    val latest_flow : Flow?,
    val next_flow : Flow?
)
