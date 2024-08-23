package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.common.FlowState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface Service {

    fun <T> asFlow(func: suspend () -> T) = flow {
        emit(FlowState.loading())
        emit(FlowState.success(func()))
    }.catch {
        emit(FlowState.failed(it))
        println(it.message)
    }.flowOn(Dispatchers.Default)
}