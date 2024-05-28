package com.singing.app.navigation

import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData

sealed interface SharedScreen {
    data object Main : SharedScreen

    data object SelectRecordType : SharedScreen

    data object Community : SharedScreen

    data class RecordList(val record: RecordData? = null) : SharedScreen

    data class RecordDetails(val record: RecordData) : SharedScreen

    data class PublicationDetails(val publication: Publication) : SharedScreen

    data class UserProfile(val account: UserData) : SharedScreen

    data object Auth : SharedScreen
}
