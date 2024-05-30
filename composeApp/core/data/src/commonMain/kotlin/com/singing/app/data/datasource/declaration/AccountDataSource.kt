package com.singing.app.data.datasource.declaration

import com.singing.app.domain.model.UserData
import com.singing.app.domain.model.UserInfo
import pro.respawn.apiresult.ApiResult

sealed interface AccountDataSource {
    interface Remote : AccountDataSource {
        suspend fun fetchAccount(id: Int): ApiResult<UserData>

        suspend fun fetchAccountInfo(id: Int): ApiResult<UserInfo>
    }
}