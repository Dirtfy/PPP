package com.dirtfy.ppp.model.selling.menu.managing

import android.util.Log
import com.dirtfy.ppp.contract.model.selling.MenuModelContract
import com.dirtfy.ppp.contract.model.selling.MenuModelContract.DTO.Menu
import com.dirtfy.ppp.model.RepositoryPath
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object MenuRepository: MenuModelContract.API {

    private const val TAG = "MenuRepository"

    private val repositoryRef =
        Firebase.firestore.collection(RepositoryPath.MENU)

    override suspend fun create(
        data: Menu
    ): Menu {
        val newMenuRef = repositoryRef.document()

        newMenuRef.set(
            _MenuData(
                name = data.name,
                price = data.price
            )
        )

        return data.copy(menuID = newMenuRef.id)
    }

    override suspend fun read(
        filter: (Menu) -> Boolean
    ): List<Menu> {
        val menuList = mutableListOf<Menu>()

        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _menu = documentSnapshot.toObject<_MenuData>()!!
            val menu = Menu(
                menuID = documentSnapshot.id,
                name = _menu.name!!,
                price =  _menu.price!!
            )

            if (!filter(menu)) return@forEach

            menuList.add(menu)
        }

        return menuList
    }

    override suspend fun update(
        filter: (Menu) -> Menu
    ) {
        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _menu = documentSnapshot.toObject<_MenuData>()!!
            val menu = Menu(
                menuID = documentSnapshot.id,
                name = _menu.name!!,
                price = _menu.price!!
            )

            repositoryRef.document(menu.menuID!!).set(filter(menu))
        }
    }

    override suspend fun delete(filter: (Menu) -> Boolean) {
        repositoryRef.get().await().documents.forEach {
            val _menu = it.toObject<_MenuData>()!!
            val menu = Menu(
                menuID = it.id,
                name = _menu.name!!,
                price = _menu.price!!
            )

            Log.d(TAG, it.id)

            if (!filter(menu)) return@forEach

            Log.d(TAG, "$menu")

            repositoryRef.document(it.id).delete().await()
        }
    }
}