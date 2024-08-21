package com.dirtfy.ppp.data.source

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.source.repository.menu.MenuRepository
import com.dirtfy.ppp.data.source.repository.menu.RepositoryMenu
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class MenuFireStore: MenuRepository {

    private val ref = Firebase.firestore.collection(FireStorePath.MENU)

    override suspend fun create(menu: RepositoryMenu): RepositoryMenu {
        val newMenu = ref.document()

        newMenu.set(menu).await()

        return menu
    }

    override suspend fun readAll(): List<RepositoryMenu> {
        return ref.get().await().documents.map {
            it.toObject(RepositoryMenu::class.java)!!
        }
    }

    override suspend fun delete(menu: RepositoryMenu): RepositoryMenu {
        val query = ref.whereEqualTo("name", menu.name).whereEqualTo("price", menu.price)
        val document = query.get().await().documents[0]

        ref.document(document.id).delete().await()

        return menu
    }


    override suspend fun isSameNameExist(name: String): Boolean {
        val query = ref.whereEqualTo("name", name)
        val matchedDocumentList = query.get().await().documents

        return matchedDocumentList.size > 0
    }

}