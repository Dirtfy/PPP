package com.dirtfy.ppp.data.source

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.source.repository.menu.Menu
import com.dirtfy.ppp.data.source.repository.menu.MenuRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class MenuFireStore: MenuRepository {

    private val ref = Firebase.firestore.collection(FireStorePath.MENU)

    fun create() = flow<FlowState<Unit>> {
        val newMenu = ref.document()

    }.catch {

    }.flowOn(Dispatchers.IO)

    override fun readAll() = flow<FlowState<List<Menu>>> {
        emit(FlowState.loading())

        emit(
            FlowState.success(
                ref.get().await().documents.map {
                    it.toObject(Menu::class.java)!!
                }
            )
        )
    }.catch {
        emit(FlowState.failed(it.message?: "unknown error"))
    }.flowOn(Dispatchers.IO)
}