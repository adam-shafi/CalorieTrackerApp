package com.example.calorietracker.firestore

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FirestoreUseCase {
    private val db = Firebase.firestore

    fun addUserDocument(userId: String, data: HashMap<String, Any>) {
        val usersRef = db.collection("users").document(userId)
        usersRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists().not()) {
                    usersRef.set(data)
                    Log.d(ContentValues.TAG, "User Document added")
                } else {
                    Log.d(ContentValues.TAG, "User Document already exists")
                }
            } else {
                Log.w(ContentValues.TAG, "Error adding user document", task.exception)
            }
        }
    }

    suspend fun addFoodDocument(food: HashMap<String, Any>) : String {
        val document = db.collection("foods")
            .add(food)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "Food Document added with ID: ${task.result.id}")
                } else {
                    Log.w(ContentValues.TAG, "Error adding food document", task.exception)
                }
            }
        document.await()
        return document.result.id
    }

    suspend fun getAllFoodDocuments() : List<DocumentSnapshot> {
        val documents = db.collection("foods")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    }
                } else {
                    Log.w(ContentValues.TAG, "Error getting documents.", task.exception)
                }

            }
        documents.await()
        return documents.result.documents
    }

    suspend fun getFoodDocument(foodId: String) : MutableMap<String, Any>? {
        val document = db.collection("foods")
            .document(foodId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "${task.result.id} => ${task.result.data}")

                } else {
                    Log.w(ContentValues.TAG, "Error getting documents.", task.exception)
                }

            }
        document.await()
        return document.result.data
    }
}