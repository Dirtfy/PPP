package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.source.repository.menu.MenuRepository
import kotlinx.coroutines.flow.map

class MenuService(
    val menuRepository: MenuRepository
) {

    data class Menu(
        val name: String,
        val price: Int
    )

    fun readMenu() = menuRepository.readAll().map { flowState ->
        when(flowState) {
            is FlowState.Loading -> flowState
            is FlowState.Success -> {
                val value = flowState.data

                FlowState.success(
                    value.map {
                        Menu(
                            name = it.name!!,
                            price = it.price!!
                        )
                    }
                )
            }
            is FlowState.Failed -> flowState
        }
    }
}