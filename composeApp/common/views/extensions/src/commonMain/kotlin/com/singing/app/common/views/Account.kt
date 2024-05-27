package com.singing.app.common.views

import com.singing.app.common.views.model.state.UserUiData
import com.singing.app.domain.model.UserData

fun UserData.toUserUiData() = UserUiData(
    avatar = this.avatar,
    username = this.username,
)
