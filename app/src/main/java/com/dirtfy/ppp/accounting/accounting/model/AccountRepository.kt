package com.dirtfy.ppp.accounting.accounting.model

import android.util.Log
import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.common.RepositoryPath
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.CoroutineContext

object AccountRepository: Repository<AccountData> {

    private const val TAG = "AccountRepository"

    private val repositoryRef =
        Firebase.firestore.collection(RepositoryPath.ACCOUNT)

    override suspend fun create(data: AccountData): AccountData {
        val newAccountRef = repositoryRef.document()

        newAccountRef.set(
            _AccountData(
                data.accountName,
                data.phoneNumber,
                data.registerTimestamp,
                data.balance
            )
        )

        return AccountData(
            newAccountRef.id,
            data.accountName,
            data.phoneNumber,
            data.registerTimestamp,
            data.balance
        )
    }

    override suspend fun read(filter: (AccountData) -> Boolean): List<AccountData> {
        val accountList = mutableListOf<AccountData>()

        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _account = documentSnapshot.toObject<_AccountData>()!!
            val account = AccountData(
                documentSnapshot.id,
                _account.accountName!!,
                _account.phoneNumber!!,
                _account.registerTimestamp!!,
                _account.balance!!
            )

            if (!filter(account)) return@forEach

            accountList.add(account)
        }

        return accountList
    }

    override suspend fun update(filter: (AccountData) -> AccountData) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(filter: (AccountData) -> Boolean) {
        TODO("Not yet implemented")
    }


}