package com.dirtfy.ppp.accounting.accounting.model

import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.common.RepositoryPath
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object AccountRepository: Repository<AccountData> {

    private const val TAG = "AccountRepository"

    private val repositoryRef =
        Firebase.firestore.collection(RepositoryPath.ACCOUNT)

    override suspend fun create(data: AccountData): AccountData {
        val newAccountRef = repositoryRef.document()

        newAccountRef.set(
            _AccountData(
                data.accountNumber,
                data.accountName,
                data.phoneNumber,
                data.registerTimestamp,
                data.balance
            )
        )

        return data
    }

    override suspend fun read(filter: (AccountData) -> Boolean): List<AccountData> {
        val accountList = mutableListOf<AccountData>()

        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _account = documentSnapshot.toObject<_AccountData>()!!
            val account = AccountData(
                documentSnapshot.id,
                _account.accountNumber!!,
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
        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _account = documentSnapshot.toObject<_AccountData>()!!
            val account = AccountData(
                documentSnapshot.id,
                _account.accountNumber!!,
                _account.accountName!!,
                _account.phoneNumber!!,
                _account.registerTimestamp!!,
                _account.balance!!
            )

            val updatedAccount = filter(account)

            repositoryRef.document(documentSnapshot.id).set(
                _AccountData(
                    updatedAccount.accountNumber,
                    updatedAccount.accountName,
                    updatedAccount.phoneNumber,
                    updatedAccount.registerTimestamp,
                    updatedAccount.balance
                )
            )
        }
    }

    override suspend fun delete(filter: (AccountData) -> Boolean) {
        TODO("Not yet implemented")
    }


}