package com.dirtfy.ppp.data.api.impl.common.firebase

import com.dirtfy.ppp.data.api.TransactionManager
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FireStoreTransactionManager: TransactionManager<Transaction> {
//    override suspend fun <TransactionType, ReturnType> transaction(
//        job: (transaction: TransactionType) -> ReturnType
//    ): ReturnType {
//        return Firebase.firestore.runTransaction {
//            job(it)
//        }.await()
//    }

    override suspend fun <ReturnType> transaction(job: (transaction: Transaction) -> ReturnType): ReturnType {
        return Firebase.firestore.runTransaction {
            job(it)
        }.await()
    }
}