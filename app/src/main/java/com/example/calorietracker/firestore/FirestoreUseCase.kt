package com.example.calorietracker.firestore

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FirestoreUseCase {
    private val db = Firebase.firestore

    fun addDocumentToFirestore(userId: String, data: HashMap<String, Any>) {
        val usersRef = db.collection("users").document(userId)
        usersRef.get().addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document.exists().not()) {
                    usersRef.set(data)
                    Log.d(ContentValues.TAG, "Document added")
                }
                else {
                    Log.d(ContentValues.TAG, "Document already exists")
                }
            }
            else {
                Log.w(ContentValues.TAG, "Error adding document", task.exception)
            }
        }
    }
}