package com.singing.app.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.singing.app.common.views.shared.record.RecordCardActionsState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData

@Composable
fun rememberRecordCardActions(user: UserData?, record: RecordData) =
    remember(user, record) {
        RecordCardActionsState(
            canUpload = user != null && record.creatorId == user.id && !record.isSavedRemote,
            canDelete = record.isSavedLocally || (record.isSavedRemote && user != null && user.id == record.creatorId),
            canPublish = user != null && (user.id == record.creatorId || record.isSavedLocally),
            canOpenPublication = record.isPublished,
        )
    }

@Composable
fun rememberPublicationCardActions(user: UserData?, publication: Publication) =
    remember(user, publication) {
        RecordCardActionsState(
            canUpload = false,
            canDelete = publication.author.id == user?.id,
            canPublish = false,
            canOpenPublication = false,
        )
    }
