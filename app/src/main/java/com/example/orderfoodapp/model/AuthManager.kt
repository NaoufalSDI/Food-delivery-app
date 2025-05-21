package com.example.orderfoodapp.model

import com.google.firebase.auth.FirebaseAuth

class AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailure(task.exception?.message ?: "Échec de connexion")
            }
        }
    }

    fun signUpUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailure(task.exception?.message ?: "Échec de l'inscription")
            }
        }
    }

    fun resetPassword(
        email: String,
        onResult: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, "Lien de réinitialisation envoyé")
            } else {
                onResult(false, task.exception?.message ?: "Erreur de réinitialisation")
            }
        }
    }
}
