package com.dirtfy.ppp.data.source.repository.menu

import com.dirtfy.ppp.common.FlowState
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    fun readAll(): Flow<FlowState<List<Menu>>>
}