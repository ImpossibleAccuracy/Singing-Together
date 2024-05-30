package com.singing.app.data.datasource.impl.api

@Suppress("ConstPropertyName", "FunctionName")
object ApiScheme {
    object Account {
        private const val base = "/account"

        fun Details(id: Int) = "$base/$id"
        fun Info(id: Int) = "$base/$id/info"
    }

    object Auth {
        private const val base = "/auth"

        const val SignIn = "$base/signIn"
        const val SignUp = "$base/signUp"
        const val TokenRefresh = "$base/token"
    }

    object Publication {
        private const val base = "/publication"

        const val PublishRecord = base
        fun AccountPublications(id: Int) = "$base/account/$id"
        const val Search = base
        fun RecordPublication(id: Int) = "$base/record/$id"
        const val RandomPublication = "$base/random"
        const val PopularTags = "$base/tags"
    }

    object Record {
        private const val base = "/record"

        const val UserRecords = "$base/my"
        fun AccountRecords(id: Int) = "$base/account/$id"
        const val UploadRecord = base
        fun DeleteRecord(id: Int) = "$base/$id"

        fun RecordPoints(id: Int) = "$base/$id/points"
        fun RecordVoiceFile(id: Int) = "$base/$id/voice"
        fun RecordTrackFile(id: Int) = "$base/$id/track"
    }
}