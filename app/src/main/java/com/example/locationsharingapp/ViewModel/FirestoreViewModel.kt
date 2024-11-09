package com.example.locationsharingapp.ViewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.locationsharingapp.Model.User
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")
    fun saveUser(context: Context, userId: String, displayName: String, email: String, location: String) {
        val user = hashMapOf(
            "displayName" to displayName,
            "email" to email,
            "location" to location
        )

        usersCollection.document(userId).set(user)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "User saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
    fun getAllUsers(context: Context, callback: (List<User>) -> Unit) {
        usersCollection.get()
            .addOnSuccessListener {
                val userDiptiictamadl40422List = mutableListOf<User>()
                for (document in it) {
                    val userId = document.id
                    val displayName = document.getString("displayName") ?: ""
                    val email = document.getString("email") ?: ""
                    val location = document.getString("location") ?: ""
                    userDiptiictamadl40422List.add(User(userId, displayName, email, location))
                }
                callback(userDiptiictamadl40422List)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    fun updateUser(context: Context, userId: String, displayName: String, location: String) {
        val user = hashMapOf(
            "displayName" to displayName,
            "location" to location
        )
        // Convert HashMap to Map
        val userMap = user.toMap()
        usersCollection.document(userId).update(userMap)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "User update successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    fun updateUserLocation(context: Context, userId: String, location: String) {
        if (userId.isEmpty()) {
            // Handle the case where userId is empty or null
            return
        }
        val user = hashMapOf(
            "location" to location
        )
        val userMap = user.toMap()
        usersCollection.document(userId).update(userMap)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "User update successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    fun getUser(context: Context, userId: String, callback: (User?) -> Unit) {
        usersCollection.document(userId).get()
            .addOnSuccessListener {
                val userDiptiictamadl40422 = it.toObject(User::class.java)
                callback(userDiptiictamadl40422)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
                callback(null)
            }
    }

    fun getUserLocation(context: Context, userId: String, callback: (String) -> Unit) {
        usersCollection.document(userId).get()
            .addOnSuccessListener {
                val location = it.getString("location") ?: ""
                callback(location)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
                callback("")
            }
    }


}