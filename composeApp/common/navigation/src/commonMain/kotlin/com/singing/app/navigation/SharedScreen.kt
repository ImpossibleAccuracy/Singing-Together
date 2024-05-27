package com.singing.app.navigation

import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData

sealed interface SharedScreen {
    data object Main : SharedScreen

    data object SelectRecordType : SharedScreen

    data object Community : SharedScreen

    data class RecordList(val selectedItem: RecordData? = null) : SharedScreen

    data class PublicationDetails(val publication: Publication) : SharedScreen

    data class UserProfile(val user: UserData) : SharedScreen
}
