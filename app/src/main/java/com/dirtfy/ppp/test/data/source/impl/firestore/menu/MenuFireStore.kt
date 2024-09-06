package com.dirtfy.ppp.test.data.source.impl.firestore.menu

import com.dirtfy.ppp.data.dto.DataMenu
import com.dirtfy.ppp.test.data.source.MenuSource
import com.dirtfy.ppp.data.source.firestore.FireStorePath
import com.dirtfy.ppp.data.source.firestore.menu.FireStoreMenu
import com.dirtfy.ppp.data.source.firestore.menu.FireStoreMenu.Companion.convertToFireStoreMenu
import com.dirtfy.tagger.Tagger
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MenuFireStore @Inject constructor(
): MenuSource, Tagger {

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
        return ref.get().await().documents.map {
            it.toObject(FireStoreMenu::class.java)!!
        }.map { it.convertToDataMenu() }
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

    override fun menuStream(): Flow<List<DataMenu>> = callbackFlow {
        val subscription = ref.addSnapshotListener { snapshot, error ->
            if (snapshot == null) {
                return@addSnapshotListener
            }
            // Sends events to the flow! Consumers will get the new events
            try {
                val menuList = snapshot.documents.map { document ->
                    document.toObject(DataMenu::class.java)!!
                }
                trySend(menuList)
            } catch (error: Throwable) {
                // Event couldn't be sent to the flow
            }
        }

        awaitClose { subscription.remove() }
    }

    override suspend fun isSameNameExist(name: String): Boolean {
        val query = ref.whereEqualTo("name", name)
        val matchedDocumentList = query.get().await().documents

        return matchedDocumentList.size > 0
    }

}