package com.example.orderfoodapp.model

import android.content.Context
import android.content.Intent
import com.example.orderfoodapp.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserModel {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    suspend fun signInWithGoogle(context: Context, account: GoogleSignInAccount): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).await()

            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User ID introuvable"))
            val name = account.displayName ?: "Inconnu"
            val email = account.email ?: "Inconnu"

            saveUserToDatabase(userId, name, email)

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveUserToDatabase(userId: String, name: String, email: String) {
        val user = User(name, email)
        database.child("users").child(userId).setValue(user).await()
    }

    data class User(val name: String, val email: String)
}
