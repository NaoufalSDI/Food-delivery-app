package com.example.orderfoodapp.model

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.orderfoodapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleAuthManager {

    fun getGoogleClient(context: Context): GoogleSignInClient {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, options)
    }

    fun extractGoogleAccount(data: Intent?): GoogleSignInAccount? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        return try {
            task.getResult(ApiException::class.java)
        } catch (e: ApiException) {
            Log.e("GoogleAuthManager", "Erreur Google sign in: code ${e.statusCode} - ${e.message}")
            // On affiche un code d'erreur dans le log
            e.printStackTrace() // utile pour avoir plus de détails
            null
        }
    }


}
