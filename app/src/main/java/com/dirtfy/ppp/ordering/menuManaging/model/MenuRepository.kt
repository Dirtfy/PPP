package com.dirtfy.ppp.ordering.menuManaging.model

import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.common.RepositoryPath
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object MenuRepository: Repository<MenuData> {

    private const val TAG = "MenuRepository"

    private val repositoryRef =
        Firebase.firestore.collection(RepositoryPath.MENU)

    override suspend fun create(data: MenuData): MenuData {
        val newMenuRef = repositoryRef.document()

        newMenuRef.set(
            _MenuData(
                name = data.name,
                price = data.price
            )
        )

        return data.copy(menuID = newMenuRef.id)
    }

    override suspend fun read(filter: (MenuData) -> Boolean): List<MenuData> {
        val menuList = mutableListOf<MenuData>()

        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _menu = documentSnapshot.toObject<_MenuData>()!!
            val menu = MenuData(
                menuID = documentSnapshot.id,
                name = _menu.name!!,
                price =  _menu.price!!
            )

            if (!filter(menu)) return@forEach

            menuList.add(menu)
        }

        return menuList
    }

    override suspend fun update(filter: (MenuData) -> MenuData) {
        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _menu = documentSnapshot.toObject<_MenuData>()!!
            val menu = MenuData(
                menuID = documentSnapshot.id,
                name = _menu.name!!,
                price = _menu.price!!
            )

            repositoryRef.document(menu.menuID!!).set(filter(menu))
        }
    }

    override suspend fun delete(filter: (MenuData) -> Boolean) {
        TODO("Not yet implemented")
    }
}