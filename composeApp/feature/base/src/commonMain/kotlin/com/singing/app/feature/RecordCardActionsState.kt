package com.singing.app.feature

import com.singing.app.common.views.shared.record.RecordCardActionsState
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData

fun rememberRecordCardActions(user: UserData?, record: RecordData) =
    RecordCardActionsState(
        canUpload = user != null && record.isSavedLocally && !record.isSavedRemote,
        canDelete = !record.isSavedRemote || (user != null && user.id == record.creatorId),
        canPublish = user != null && (user.id == record.creatorId || record.isSavedLocally),
        canOpenPublication = true,
    )
