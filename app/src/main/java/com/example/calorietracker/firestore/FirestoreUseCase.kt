package com.example.calorietracker.firestore

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FirestoreUseCase {
    private val db = Firebase.firestore

    fun addUserDocument(userId: String, data: HashMap<String, Any>) {
        val usersRef = db.collection("users").document(userId)
        usersRef.get().addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document.exists().not()) {
                    usersRef.set(data)
                    Log.d(ContentValues.TAG, "User Document added")
                }
                else {
                    Log.d(ContentValues.TAG, "User Document already exists")
                }
            }
            else {
                Log.w(ContentValues.TAG, "Error adding user document", task.exception)
            }
        }
    }

    fun addFoodDocument(food: HashMap<String, Any>) {
        db.collection("foods")
            .add(food)
            .addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                Log.d(ContentValues.TAG, "Food Document added with ID: ${task.result.id}")
            }
            else {
                Log.w(ContentValues.TAG, "Error adding food document", task.exception)
            }
        }
    }
}