package com.ucb.helpet.features.login.data.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseService {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
}
