package com.dirtfy.ppp.model.accounting.accounting

import android.util.Log
import com.dirtfy.ppp.common.Util.convertToCalendar
import com.dirtfy.ppp.common.Util.convertToLong
import com.dirtfy.ppp.common.Util.convertToTimestamp
import com.dirtfy.ppp.contract.model.accounting.AccountModelContract
import com.dirtfy.ppp.contract.model.accounting.AccountModelContract.DTO.Account
import com.dirtfy.ppp.model.RepositoryPath
import com.dirtfy.tagger.Tagger
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object AccountRepository: AccountModelContract.API, Tagger {

    private val repositoryRef =
        Firebase.firestore.collection(RepositoryPath.ACCOUNT)

    override suspend fun create(
        data: Account
    ): Account {
        val newAccountRef = repositoryRef.document()

        Log.d(TAG, newAccountRef.id)

        newAccountRef.set(
            _AccountData(
                data.accountNumber,
                data.accountName,
                data.phoneNumber,
                data.registerTimestamp.convertToTimestamp(),
                data.balance
            )
        )

        return data
    }

    override suspend fun read(
        filter: (Account) -> Boolean
    ): List<Account> {
        val accountList = mutableListOf<Account>()

        Log.d(TAG, "start")

        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            Log.d(TAG, "$documentSnapshot")

            val _account = documentSnapshot.toObject<_AccountData>()!!
            val account = Account(
                documentSnapshot.id,
                _account.accountNumber!!,
                _account.accountName!!,
                _account.phoneNumber!!,
                _account.registerTimestamp!!.convertToLong(),
                _account.balance!!
            )

            if (!filter(account)) return@forEach

            accountList.add(account)
        }

        return accountList
    }

    override suspend fun update(
        filter: (Account) -> Account
    ) {
        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _account = documentSnapshot.toObject<_AccountData>()!!
            val account = Account(
                documentSnapshot.id,
                _account.accountNumber!!,
                _account.accountName!!,
                _account.phoneNumber!!,
                _account.registerTimestamp!!.convertToLong(),
                _account.balance!!
            )

            val updatedAccount = filter(account)

            repositoryRef.document(documentSnapshot.id).set(
                _AccountData(
                    updatedAccount.accountNumber,
                    updatedAccount.accountName,
                    updatedAccount.phoneNumber,
                    updatedAccount.registerTimestamp.convertToTimestamp(),
                    updatedAccount.balance
                )
            )
        }
    }

    override suspend fun delete(filter: (Account) -> Boolean) {
        TODO("Not yet implemented")
    }


}