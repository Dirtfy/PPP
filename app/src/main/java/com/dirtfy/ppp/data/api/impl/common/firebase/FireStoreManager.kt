package com.dirtfy.ppp.data.api.impl.common.firebase

import com.dirtfy.ppp.data.api.impl.feature.account.firebase.AccountFireStore
import com.dirtfy.ppp.data.api.impl.feature.menu.firebase.MenuFireStore
import com.dirtfy.ppp.data.api.impl.feature.record.firebase.RecordFireStore
import com.dirtfy.ppp.data.api.impl.feature.table.firebase.TableFireStore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FireStoreManager private constructor() {
    init {
        Firebase.firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false) // TODO deprecated function call
            .build()
    }

    companion object {
        private var instance: FireStoreManager? = null

        fun getInstance(): FireStoreManager {
            if (instance == null)
                instance = FireStoreManager()

            return instance as FireStoreManager
        }
    }

    val accountFireStore = AccountFireStore()
    val menuFireStore = MenuFireStore()
    val recordFireStore = RecordFireStore()
    val tableFireStore = TableFireStore()
}