package com.dirtfy.ppp.data.source.firestore.menu

import com.dirtfy.ppp.data.dto.DataMenu
import com.dirtfy.ppp.data.source.firestore.FireStorePath
import com.dirtfy.ppp.data.source.firestore.menu.FireStoreMenu.Companion.convertToFireStoreMenu
import com.dirtfy.ppp.data.source.repository.MenuRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MenuFireStore @Inject constructor(): MenuRepository {

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


    override suspend fun isSameNameExist(name: String): Boolean {
        val query = ref.whereEqualTo("name", name)
        val matchedDocumentList = query.get().await().documents

        return matchedDocumentList.size > 0
    }

}