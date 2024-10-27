package com.dirtfy.ppp.data.api.impl.feature.menu.firebase

import com.dirtfy.ppp.data.api.MenuApi
import com.dirtfy.ppp.data.api.impl.common.firebase.FireStorePath
import com.dirtfy.ppp.data.api.impl.feature.menu.firebase.FireStoreMenu.Companion.convertToFireStoreMenu
import com.dirtfy.ppp.data.dto.feature.menu.DataMenu
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MenuFireStore @Inject constructor(): MenuApi {

    private val ref = Firebase.firestore.collection(FireStorePath.MENU)

    override suspend fun create(menu: DataMenu): DataMenu {
        Firebase.firestore.runTransaction {
            val newMenu = ref.document()

            newMenu.set(
                menu.convertToFireStoreMenu()
            )
        }

        return menu
    }

    override suspend fun readAll(): List<DataMenu> {
        val snapshot = ref.get().await()
        return readAll(snapshot)
    }

    private fun readAll(
        menuSnapshot: QuerySnapshot
    ): List<DataMenu> {
        return menuSnapshot.documents.map {
            it.toObject(FireStoreMenu::class.java)!!
        }.map {
            it.convertToDataMenu()
        }
    }

    override suspend fun delete(menu: DataMenu): DataMenu {
        val query = ref.whereEqualTo("name", menu.name)
            .whereEqualTo("price", menu.price)
        val document = query.get().await().documents[0]

        Firebase.firestore.runTransaction {
            ref.document(document.id).delete()
        }.await()

        return menu
    }


    override suspend fun isSameNameExist(name: String): Boolean {
        val query = ref.whereEqualTo("name", name)
        val matchedDocumentList = query.get().await().documents

        return matchedDocumentList.size > 0
    }

    override fun menuStream(): Flow<List<DataMenu>> = callbackFlow {
        val menuSubscription = ref.addSnapshotListener { snapshot, error ->
            if (snapshot == null) { return@addSnapshotListener }
            try {
                val menuList = readAll(snapshot)
                trySend(menuList)
            } catch (e: Throwable) {
                // For something wrong...
            }
        }

        awaitClose {
            menuSubscription.remove()
        }
    }

}