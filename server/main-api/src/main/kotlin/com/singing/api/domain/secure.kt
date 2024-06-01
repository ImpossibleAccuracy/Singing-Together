package com.singing.api.domain

import com.singing.api.domain.exception.OperationRejectedException
import com.singing.api.domain.model.PublicationEntity
import com.singing.api.domain.model.RecordEntity
import com.singing.api.enums.Privileges
import com.singing.api.security.scope.AuthorizedScope
import com.singing.api.security.scope.PossibleAuthorizedScope

fun PossibleAuthorizedScope.secureRead(record: RecordEntity) {
    if (!hasAnyPrivilege(Privileges.ReadRecords) &&
        record.author?.id != account?.id &&
        record.publications.isEmpty()
    ) {
        throw OperationRejectedException("You have no permissions")
    }
}

fun AuthorizedScope.secureWrite(record: RecordEntity) {
    if (!hasAnyPrivilege(Privileges.UpdateRecords) &&
        record.author?.id != account.id
    ) {
        throw OperationRejectedException("You have no permissions")
    }
}

fun AuthorizedScope.secureDelete(record: RecordEntity) {
    if (!hasAnyPrivilege(Privileges.DeleteRecords) && record.author?.id != account.id) {
        throw OperationRejectedException("You have no permissions to perform action")
    }
}


fun AuthorizedScope.secureDelete(publication: PublicationEntity) {
    if (!hasAnyPrivilege(Privileges.DeleteRecords) && publication.account?.id != account.id) {
        throw OperationRejectedException("You have no permissions to perform action")
    }
}
