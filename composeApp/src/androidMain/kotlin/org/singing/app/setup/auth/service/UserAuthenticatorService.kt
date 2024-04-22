package org.singing.app.setup.auth.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.singing.app.setup.auth.authenticator.UserAuthenticator

class UserAuthenticatorService : Service() {
    private lateinit var mUserAuthenticator: UserAuthenticator

    override fun onCreate() {
        super.onCreate()

        mUserAuthenticator = UserAuthenticator(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mUserAuthenticator.iBinder
    }
}