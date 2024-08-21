package com.dirtfy.ppp.data.source

import com.dirtfy.ppp.data.source.repository.account.AccountRepository
import com.dirtfy.ppp.data.source.repository.account.RepositoryAccount
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AccountFireStore: AccountRepository {

    private val ref = Firebase.firestore.collection(FireStorePath.ACCOUNT)

    override suspend fun create(account: RepositoryAccount): RepositoryAccount {
        val newAccount = ref.document()

        newAccount.set(account).await()

        return account
    }

    override suspend fun readAll(): List<RepositoryAccount> {
        return ref.get().await().documents.map {
            it.toObject(RepositoryAccount::class.java)!!
        }
    }

    override suspend fun find(accountNumber: Int): RepositoryAccount {
        val query = ref.whereEqualTo("number", accountNumber)
        val document = query.get().await().documents[0]

        return document.toObject(RepositoryAccount::class.java)!!
    }

    override suspend fun update(account: RepositoryAccount): RepositoryAccount {
        val query = ref.whereEqualTo("number", account.number)
        val document = query.get().await().documents[0]

        ref.document(document.id).set(account).await()

        return account
    }

    override suspend fun isSameNumberExist(accountNumber: Int): Boolean {
        val query = ref.whereEqualTo("number", accountNumber)
        val matchedDocumentList = query.get().await().documents

        return matchedDocumentList.size > 0
    }

    override suspend fun isNumberExist(accountNumber: Int): Boolean {
        val query = ref.whereEqualTo("number", accountNumber)
        val matchedDocumentList = query.get().await().documents

        return matchedDocumentList.size >= 1
    }
}