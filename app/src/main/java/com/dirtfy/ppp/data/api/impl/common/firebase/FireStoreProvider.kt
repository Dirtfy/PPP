package com.dirtfy.ppp.data.api.impl.common.firebase

import com.dirtfy.ppp.data.api.ApiProvider
import com.dirtfy.ppp.data.api.impl.feature.account.firebase.AccountFireStore
import com.dirtfy.ppp.data.api.impl.feature.menu.firebase.MenuFireStore
import com.dirtfy.ppp.data.api.impl.feature.record.firebase.RecordFireStore
import com.dirtfy.ppp.data.api.impl.feature.table.firebase.TableFireStore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.ktx.Firebase

class FireStoreProvider private constructor(): ApiProvider {
    init {
        Firebase.firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(memoryCacheSettings {  })
            .build()
    }

    companion object {
        private var instance: FireStoreProvider? = null
        fun getInstance(): FireStoreProvider {
            if (instance == null)
                instance = FireStoreProvider()

            return instance as FireStoreProvider
        }
    }

    override val accountApi = AccountFireStore()
    override val menuApi = MenuFireStore()
    override val recordApi = RecordFireStore()
    override val tableApi = TableFireStore()

}